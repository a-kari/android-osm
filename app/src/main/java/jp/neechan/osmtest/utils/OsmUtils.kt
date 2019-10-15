package jp.neechan.osmtest.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.neechan.osmtest.R
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourcePolicy
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint

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

    fun getMarkerBitmap(context: Context): Bitmap? {
        return try {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_marker)
            val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap

        } catch (e: OutOfMemoryError) {
            null
        }
    }
}
