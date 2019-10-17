package jp.neechan.osmtest.views

import android.os.Bundle
import com.neechan.osmtest.R
import jp.neechan.osmtest.models.Bookmark
import kotlinx.android.synthetic.main.activity_bookmark.*

class BookmarkActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        setupBackButton()

        val bookmark     = intent.getSerializableExtra("bookmark") as Bookmark?
        idTv.text        = bookmark?.id.toString()
        titleTv.text     = bookmark?.title
        latitudeTv.text  = bookmark?.latitude.toString()
        longitudeTv.text = bookmark?.longitude.toString()
    }
}