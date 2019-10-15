package jp.neechan.osmtest

import android.graphics.Canvas
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.neechan.osmtest.R
import jp.neechan.osmtest.utils.NetworkLocationProvider
import jp.neechan.osmtest.utils.OsmUtils
import jp.neechan.osmtest.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainActivity : AppCompatActivity() {

    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var touchOverlay: Overlay
    private var favoritePlacesOverlay: ItemizedIconOverlay<OverlayItem>? = null
    private val favoritePlaces = mutableListOf<OverlayItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PermissionUtils.checkPermissions(this)) {
            setupMap()
        }
    }

    private fun setupMap() {
        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
        setContentView(R.layout.activity_main)

        map.setTileSource(OsmUtils.tileSource)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        map.setMultiTouchControls(true)
        map.controller.setCenter(OsmUtils.getMapCenter(map.controller))

        myLocationOverlay = MyLocationNewOverlay(NetworkLocationProvider(applicationContext), map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.setPersonIcon(OsmUtils.getMyLocationMarker(this))

        showMyLocationButton.setOnClickListener {
            myLocationOverlay.enableFollowLocation()
            map.overlays.add(myLocationOverlay)
            map.invalidate()
        }

        touchOverlay = object : Overlay() {
            override fun draw(arg0: Canvas, arg1: MapView, arg2: Boolean) {}

            override fun onSingleTapConfirmed(e: MotionEvent, map: MapView): Boolean {
                val location  = map.projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                val latitude  = location.latitude
                val longitude = location.longitude

                val favoritePlace = OverlayItem("Favorite", "It's a cool place!", GeoPoint(latitude, longitude))
                favoritePlace.setMarker(OsmUtils.getFavoritePlaceMarker(applicationContext))
                favoritePlaces.add(favoritePlace)

                if (map.overlays.contains(favoritePlacesOverlay)) {
                    map.overlays.remove(favoritePlacesOverlay)
                }
                favoritePlacesOverlay = ItemizedIconOverlay(applicationContext, favoritePlaces, null)
                map.overlays.add(favoritePlacesOverlay)
                map.invalidate()
                return true
            }
        }

        map.overlays.add(touchOverlay)
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
