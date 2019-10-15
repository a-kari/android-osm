package jp.neechan.osmtest

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.neechan.osmtest.R
import jp.neechan.osmtest.utils.NetworkLocationProvider
import jp.neechan.osmtest.utils.OsmUtils
import jp.neechan.osmtest.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainActivity : AppCompatActivity() {

    private lateinit var myLocationOverlay: MyLocationNewOverlay

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
        myLocationOverlay.setPersonIcon(OsmUtils.getMarkerBitmap(this))

        showMyLocationButton.setOnClickListener {
            myLocationOverlay.enableFollowLocation()
            map.overlays.add(myLocationOverlay)
            map.invalidate()
        }
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
