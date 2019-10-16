package jp.neechan.osmtest.views

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.neechan.osmtest.R
import jp.neechan.osmtest.models.FavoritePlace
import kotlinx.android.synthetic.main.activity_favorite_place.*

class FavoritePlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_place)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val favoritePlace = intent.getSerializableExtra("favoritePlace") as FavoritePlace?
        idTv.text         = favoritePlace?.id.toString()
        titleTv.text      = favoritePlace?.title
        latitudeTv.text   = favoritePlace?.latitude.toString()
        longitudeTv.text  = favoritePlace?.longitude.toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true

        } else {
            super.onOptionsItemSelected(item)
        }
    }
}