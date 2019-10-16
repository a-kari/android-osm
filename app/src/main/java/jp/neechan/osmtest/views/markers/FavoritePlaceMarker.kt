package jp.neechan.osmtest.views.markers

import jp.neechan.osmtest.models.FavoritePlace
import jp.neechan.osmtest.utils.OsmUtils
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class FavoritePlaceMarker(private val favoritePlace:           FavoritePlace,
                          private val map:                     MapView,
                          private val markerClickCallback:     MarkerClickCallback,
                          private val infoWindowClickCallback: FavoritePlaceInfoWindow.InfoWindowClickCallback) : Marker(map) {

    interface MarkerClickCallback {
        fun onMarkerClick(placeMarker: FavoritePlaceMarker)
    }

    init {
        id       = favoritePlace.id.toString()
        title    = favoritePlace.title
        position = GeoPoint(favoritePlace.latitude, favoritePlace.longitude)
        icon     = OsmUtils.getFavoritePlaceMarker(map.context)
        setInfoWindow(FavoritePlaceInfoWindow(map, infoWindowClickCallback))
    }

    override fun onMarkerClickDefault(marker: Marker?, map: MapView?): Boolean {
        super.onMarkerClickDefault(marker, map)
        markerClickCallback.onMarkerClick(this)
        return true
    }
}