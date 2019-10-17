package jp.neechan.samplebookmarks.views

import android.os.Bundle
import jp.neechan.samplebookmarks.R
import jp.neechan.samplebookmarks.models.Bookmark
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