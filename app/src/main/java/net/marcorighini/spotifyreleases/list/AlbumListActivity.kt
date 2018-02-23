package net.marcorighini.spotifyreleases.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_album_list.*
import kotlinx.android.synthetic.main.album_list.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.component
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.viewModelProvider
import timber.log.Timber

class AlbumListActivity : AppCompatActivity(), AlbumListAdapter.AlbumClickListener, AlbumListAdapter.AlbumFavouriteToggleListener {
    private lateinit var adapter: AlbumListAdapter
    private val viewModel by viewModelProvider { component.listViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        album_list_refresh.setOnRefreshListener { viewModel.refresh() }

        viewModel.uiActions.observe(this) { it(this) }
        viewModel.liveData.observe(this) {
            when (it.response) {
                Resource.Empty -> {
                    album_list_refresh.isRefreshing = false
                    album_list_refresh.isEnabled = true
                }
                Resource.Loading -> {
                    album_list_refresh.isRefreshing = true
                    album_list_refresh.isEnabled = false
                }
                is Resource.Error -> {
                    album_list_refresh.isRefreshing = false
                    album_list_refresh.isEnabled = true
                    Timber.w(it.response.message)
                    Toast.makeText(this, "Error loading New Released Albums", Toast.LENGTH_LONG).show()
                }
                is Resource.Success -> {
                    album_list_refresh.isRefreshing = false
                    album_list_refresh.isEnabled = true
                    adapter.replace(it.response.data, it.favourites)
                }
            }
        }

        adapter = AlbumListAdapter(this, null,  null, this, this)
        album_list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onAlbumClick(album: AlbumSimple) {
        viewModel.onAlbumClick(album)
    }

    override fun onAlbumFavouriteToggle(album: AlbumSimple) {
        viewModel.onFavouriteToggle(album)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_favourites -> viewModel.onFavouriteClick()
            else -> {
            }
        }

        return true
    }
}
