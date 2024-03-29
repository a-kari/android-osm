package jp.neechan.samplebookmarks.views

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProviders
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.neechan.samplebookmarks.R
import jp.neechan.samplebookmarks.models.Bookmark
import jp.neechan.samplebookmarks.providers.NetworkLocationProvider
import jp.neechan.samplebookmarks.utils.OsmUtils
import jp.neechan.samplebookmarks.utils.PermissionUtils
import jp.neechan.samplebookmarks.viewmodels.MapViewModel
import jp.neechan.samplebookmarks.views.markers.BookmarkInfoWindow
import jp.neechan.samplebookmarks.views.markers.BookmarkMarker
import kotlinx.android.synthetic.main.activity_map.*
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapActivity : BaseActivity(), BookmarkMarker.BookmarkClickCallback,
                    BookmarkInfoWindow.InfoWindowClickCallback {

    private lateinit var viewModel:         MapViewModel
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var bookmarksOverlay:  RadiusMarkerClusterer
    private lateinit var touchOverlay:      Overlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)

        if (PermissionUtils.checkPermissions(this)) {
            setupMap()
            setupMyLocation()
            setupBookmarks()
        }
    }

    private fun setupMap() {
        Configuration.getInstance().load(
                applicationContext,
                applicationContext.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE))
        setContentView(R.layout.activity_map)

        map.setTileSource(OsmUtils.tileSource)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        map.setMultiTouchControls(true)
        map.controller.setCenter(OsmUtils.getMapCenter(map.controller))
    }

    private fun setupMyLocation() {
        myLocationOverlay = MyLocationNewOverlay(NetworkLocationProvider(applicationContext), map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.setPersonIcon(OsmUtils.getMyLocationMarker(this))

        showMyLocationButton.setOnClickListener {
            myLocationOverlay.enableFollowLocation()
            if (map.zoomLevelDouble < OsmUtils.DEFAULT_ZOOM) {
                map.controller.setZoom(OsmUtils.DEFAULT_ZOOM)
            }
            map.overlays.add(myLocationOverlay)
            map.invalidate()
        }
    }

    private fun setupBookmarks() {
        touchOverlay = object : Overlay() {
            override fun draw(arg0: Canvas, arg1: MapView, arg2: Boolean) {}

            override fun onSingleTapConfirmed(e: MotionEvent, map: MapView): Boolean {
                val location  = map.projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                val latitude  = location.latitude
                val longitude = location.longitude

                viewModel.searchAddressByCoordinates(latitude, longitude)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { address ->
                            if (address?.address != null) {
                                val bookmark = Bookmark(address.id, address.address, latitude, longitude)
                                val marker   = BookmarkMarker(bookmark, map, this@MapActivity, this@MapActivity)
                                bookmarksOverlay.add(marker)
                                bookmarksOverlay.invalidate()
                                map.invalidate()
                            }
                        }
                return true
            }
        }
        map.overlays.add(touchOverlay)

        bookmarksOverlay = RadiusMarkerClusterer(this)
        bookmarksOverlay.setIcon(OsmUtils.getClusterMarker(this)) // Cluster icon.
        map.overlays.add(bookmarksOverlay)

        val sampleBookmarks = OsmUtils.getSampleBookmarks()
        for (sampleBookmark in sampleBookmarks) {
            bookmarksOverlay.add(BookmarkMarker(sampleBookmark, map, this, this))
        }
    }

    override fun onBookmarkClick(bookmark: BookmarkMarker) {
        for (overlay in bookmarksOverlay.items) {
            if (overlay is BookmarkMarker && overlay != bookmark) {
                overlay.closeInfoWindow()
            }
        }
    }

    override fun onInfoWindowClick(bookmarkMarker: BookmarkMarker) {
        val bookmark = Bookmark(
                            bookmarkMarker.id.toLong(),
                            bookmarkMarker.title,
                            bookmarkMarker.position.latitude,
                            bookmarkMarker.position.longitude)
        val startBookmark = Intent(this, BookmarkActivity::class.java)
        startBookmark.putExtra("bookmark", bookmark)
        startActivity(startBookmark)
    }

    public override fun onResume() {
        super.onResume()
        if (map != null) {
            map.onResume()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (map != null) {
            map.onPause()
            myLocationOverlay.disableMyLocation()
            myLocationOverlay.disableFollowLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size == 3) {
            setupMap()
        }
    }
}
