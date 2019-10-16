package jp.neechan.osmtest.views.markers

import android.widget.TextView
import com.neechan.osmtest.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow

class FavoritePlaceInfoWindow(private val map: MapView,
                              private val infoWindowClickCallback: InfoWindowClickCallback)
                              : MarkerInfoWindow(R.layout.item_favorite_place_info_window, map) {

    interface InfoWindowClickCallback {
        fun onInfoWindowClick(placeMarker: FavoritePlaceMarker)
    }

    override fun onOpen(item: Any?) {
        if (mView != null) {
            val marker   = item as FavoritePlaceMarker
            val title    = marker.title ?: ""
            val titleTv  = mView.findViewById<TextView>(R.id.titleTv)
            titleTv.text = title

            mView.setOnTouchListener(null)
            mView.setOnClickListener { infoWindowClickCallback.onInfoWindowClick(marker) }
        }
    }
}