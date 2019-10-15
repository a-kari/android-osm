package jp.neechan.osmtest.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.neechan.osmtest.R
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourcePolicy
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem

object OsmUtils {

    private const val TILE_SOURCE_URL   = "http://my.tile.server/hot/"
    private const val DEFAULT_LATITUDE  = 35.69879
    private const val DEFAULT_LONGITUDE = 139.77471
    private const val DEFAULT_ZOOM      = 12.0

    val tileSource: ITileSource
        get() = XYTileSource(
                "Neechan Tiles",
                0,
                19,
                256,
                ".png",
                arrayOf(TILE_SOURCE_URL, TILE_SOURCE_URL, TILE_SOURCE_URL),
                "© OpenStreetMap contributors",
                TileSourcePolicy(
                        2,
                        TileSourcePolicy.FLAG_NO_BULK
                                or TileSourcePolicy.FLAG_NO_PREVENTIVE
                                or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
                                or TileSourcePolicy.FLAG_USER_AGENT_NORMALIZED))

    fun getMapCenter(mapController: IMapController): GeoPoint {
        mapController.setZoom(DEFAULT_ZOOM)
        return GeoPoint(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }

    fun getMyLocationMarker(context: Context): Bitmap? {
        return try {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_marker)
            val bitmap   = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas   = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap

        } catch (e: OutOfMemoryError) {
            null
        }
    }

    fun getFavoritePlaceMarker(context: Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.ic_favorite)!!
    }

    fun getSampleFavoritePlaces(context: Context): List<OverlayItem> {
        val sampleFavoriteCoordinates = mutableListOf<Pair<Double, Double>>()
        sampleFavoriteCoordinates.add(Pair(43.21454822717907, 76.93775752702777))
        sampleFavoriteCoordinates.add(Pair(43.219253427502906, 76.91470441099585))
        sampleFavoriteCoordinates.add(Pair(43.23930244761985, 76.91812900929176))

        val sampleFavoritePlaces = mutableListOf<OverlayItem>()
        for (coordinates in sampleFavoriteCoordinates) {
            val favoritePlace = OverlayItem("Favorite", "It's a cool place!", GeoPoint(coordinates.first, coordinates.second))
            favoritePlace.setMarker(getFavoritePlaceMarker(context))
            sampleFavoritePlaces.add(favoritePlace)
        }

        return sampleFavoritePlaces
    }
}
