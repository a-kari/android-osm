package jp.neechan.osmtest.views

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.neechan.osmtest.R
import jp.neechan.osmtest.models.FavoritePlace
import jp.neechan.osmtest.providers.NetworkLocationProvider
import jp.neechan.osmtest.utils.OsmUtils
import jp.neechan.osmtest.utils.PermissionUtils
import jp.neechan.osmtest.views.markers.FavoritePlaceInfoWindow
import jp.neechan.osmtest.views.markers.FavoritePlaceMarker
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*

class MainActivity : AppCompatActivity(), FavoritePlaceMarker.MarkerClickCallback,
                     FavoritePlaceInfoWindow.InfoWindowClickCallback {

    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var touchOverlay:      Overlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PermissionUtils.checkPermissions(this)) {
            setupMap()
            setupMyLocation()
            setupFavoritePlaces()
        }
    }

    private fun setupMap() {
        Configuration.getInstance().load(
                applicationContext,
                applicationContext.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE))
        setContentView(R.layout.activity_main)

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
            map.overlays.add(myLocationOverlay)
            map.invalidate()
        }
    }

    private fun setupFavoritePlaces() {
        touchOverlay = object : Overlay() {
            override fun draw(arg0: Canvas, arg1: MapView, arg2: Boolean) {}

            override fun onSingleTapConfirmed(e: MotionEvent, map: MapView): Boolean {
                val location      = map.projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                val latitude      = location.latitude
                val longitude     = location.longitude
                val favoritePlace = FavoritePlace(Math.abs(Random().nextLong()), "Favorite", latitude, longitude)
                val marker        = FavoritePlaceMarker(favoritePlace, map, this@MainActivity, this@MainActivity)

                map.overlayManager.add(marker)
                map.invalidate()
                return true
            }
        }
        map.overlays.add(touchOverlay)

        val sampleFavoritePlaces = OsmUtils.getSampleFavoritePlaces()
        for (sampleFavoritePlace in sampleFavoritePlaces) {
            map.overlayManager.add(FavoritePlaceMarker(sampleFavoritePlace, map, this, this))
        }
    }

    override fun onMarkerClick(placeMarker: FavoritePlaceMarker) {
        for (overlay in map.overlays) {
            if (overlay is FavoritePlaceMarker && overlay != placeMarker) {
                overlay.closeInfoWindow()
            }
        }
    }

    override fun onInfoWindowClick(placeMarker: FavoritePlaceMarker) {
        val favoritePlace = FavoritePlace(
                            placeMarker.id.toLong(),
                            placeMarker.title,
                            placeMarker.position.latitude,
                            placeMarker.position.longitude)
        val startFavoritePlace = Intent(this, FavoritePlaceActivity::class.java)
        startFavoritePlace.putExtra("favoritePlace", favoritePlace)
        startActivity(startFavoritePlace)
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
