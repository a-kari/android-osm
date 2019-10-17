package jp.neechan.samplebookmarks.views

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import jp.neechan.samplebookmarks.viewmodels.ViewModelFactory
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

    protected val viewModelFactory: ViewModelFactory by inject()

    fun setupBackButton() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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