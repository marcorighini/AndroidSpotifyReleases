package net.marcorighini.spotifyreleases.favourites

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_album_list.*
import kotlinx.android.synthetic.main.album_list.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.component
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.utils.viewModelProvider


class FavouritesListActivity : AppCompatActivity(), FavouritesListAdapter.AlbumClickListener, FavouritesListAdapter.AlbumFavouriteToggleListener {
    private lateinit var adapter: FavouritesListAdapter
    private val viewModel by viewModelProvider { component.favouriteViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        album_list_refresh.isEnabled = false

        viewModel.uiActions.observe(this) { it(this) }
        viewModel.liveData.observe(this) {
            adapter.replace(it.favourites)
        }

        adapter = FavouritesListAdapter(this, null, this, this)
        album_list.adapter = adapter
    }

    override fun onAlbumClick(album: AlbumSimple) {
        viewModel.onAlbumClick(album)
    }

    override fun onAlbumFavouriteToggle(album: AlbumSimple) {
        viewModel.onFavouriteToggle(album)
    }
}
