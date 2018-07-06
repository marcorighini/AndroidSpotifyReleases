package net.marcorighini.spotifyreleases.favourites

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.album_list.*
import kotlinx.android.synthetic.main.fragment_favourite_list.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.component
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.utils.viewModelProvider


class FavouritesListFragment : Fragment(), FavouritesListAdapter.AlbumClickListener, FavouritesListAdapter.AlbumFavouriteToggleListener {
    private lateinit var adapter: FavouritesListAdapter
    private val viewModel by viewModelProvider { component.favouriteViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourite_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.title = getString(R.string.favourites)

        album_list_refresh.isEnabled = false

        viewModel.uiActions.observe(this) { it(this) }
        viewModel.liveData.observe(this) {
            adapter.replace(it.favourites)
        }

        adapter = FavouritesListAdapter(context!!, null, this, this)
        album_list.adapter = adapter
    }

    override fun onAlbumClick(album: AlbumSimple) {
        viewModel.onAlbumClick(album)
    }

    override fun onAlbumFavouriteToggle(album: AlbumSimple) {
        viewModel.onFavouriteToggle(album)
    }
}
