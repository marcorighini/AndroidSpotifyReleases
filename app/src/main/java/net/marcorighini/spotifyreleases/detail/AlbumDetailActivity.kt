package net.marcorighini.spotifyreleases.detail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_album_detail.*
import net.marcorighini.spotifyreleases.list.AlbumListActivity
import net.marcorighini.spotifyreleases.R

class AlbumDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)
        setSupportActionBar(detail_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = AlbumDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(AlbumDetailFragment.ALBUM, intent.getParcelableExtra(AlbumDetailFragment.ALBUM))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.album_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, AlbumListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
