package jp.neechan.samplebookmarks.views.markers

import android.widget.TextView
import jp.neechan.samplebookmarks.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow

class BookmarkInfoWindow(private val map: MapView,
                         private val infoWindowClickCallback: InfoWindowClickCallback)
                         : MarkerInfoWindow(R.layout.item_bookmark_info_window, map) {

    interface InfoWindowClickCallback {
        fun onInfoWindowClick(bookmarkMarker: BookmarkMarker)
    }

    override fun onOpen(item: Any?) {
        if (mView != null) {
            val marker   = item as BookmarkMarker
            val titleTv  = mView.findViewById<TextView>(R.id.titleTv)
            titleTv.text = marker.title ?: ""

            mView.setOnTouchListener(null)
            mView.setOnClickListener { infoWindowClickCallback.onInfoWindowClick(marker) }
        }
    }
}