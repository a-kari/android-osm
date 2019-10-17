package jp.neechan.samplebookmarks.views.markers

import jp.neechan.samplebookmarks.models.Bookmark
import jp.neechan.samplebookmarks.utils.OsmUtils
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class BookmarkMarker(private val bookmark:                Bookmark,
                     private val map:                     MapView,
                     private val bookmarkClickCallback:   BookmarkClickCallback,
                     private val infoWindowClickCallback: BookmarkInfoWindow.InfoWindowClickCallback) : Marker(map) {

    interface BookmarkClickCallback {
        fun onBookmarkClick(bookmark: BookmarkMarker)
    }

    init {
        id       = bookmark.id.toString()
        title    = bookmark.title
        position = GeoPoint(bookmark.latitude, bookmark.longitude)
        icon     = OsmUtils.getBookmarkMarker(map.context)
        setInfoWindow(BookmarkInfoWindow(map, infoWindowClickCallback))
    }

    override fun onMarkerClickDefault(marker: Marker?, map: MapView?): Boolean {
        super.onMarkerClickDefault(marker, map)
        bookmarkClickCallback.onBookmarkClick(this)
        return true
    }
}