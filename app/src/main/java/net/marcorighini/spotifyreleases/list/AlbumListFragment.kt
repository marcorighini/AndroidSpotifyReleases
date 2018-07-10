package net.marcorighini.spotifyreleases.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_album_list.*
import kotlinx.android.synthetic.main.album_list.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.component
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.viewModelProvider
import timber.log.Timber

class AlbumListFragment : Fragment(), AlbumListAdapter.AlbumClickListener, AlbumListAdapter.AlbumFavouriteToggleListener {
    private lateinit var adapter: AlbumListAdapter
    private val viewModel by viewModelProvider { component.listViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_album_list, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)

        album_list_refresh.setOnRefreshListener { viewModel.refresh() }

        viewModel.uiActions.observe(this) { it(this) }
        viewModel.stateLiveData.observe(this, Observer {
            when (it?.response) {
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
                    Timber.w((it.response as Resource.Error).message)
                    Toast.makeText(activity!!, "Error loading New Released Albums", Toast.LENGTH_LONG).show()
                }
                is Resource.Success -> {
                    album_list_refresh.isRefreshing = false
                    album_list_refresh.isEnabled = true
                    adapter.replace((it.response as Resource.Success).data, it.favourites)
                }
            }
        })

        adapter = AlbumListAdapter(activity!!, null,  null, this, this)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        NavigationUI.onNavDestinationSelected(item!!, findNavController())
        return super.onOptionsItemSelected(item)
    }
}
