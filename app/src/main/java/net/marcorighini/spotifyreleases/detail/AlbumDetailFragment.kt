package net.marcorighini.spotifyreleases.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_album_detail.*
import kotlinx.android.synthetic.main.fragment_album_detail.view.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.component
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.viewModelProvider
import timber.log.Timber

class AlbumDetailFragment : Fragment() {
    /**
     * arguments = Bundle().apply {
    putParcelable(AlbumDetailFragment.ALBUM, intent.getParcelableExtra(AlbumDetailFragment.ALBUM))
    }
     */

    private lateinit var albumSimple: AlbumSimple
    private val viewModel by viewModelProvider { component.detailViewModel() }
    private lateinit var adapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        albumSimple = arguments!!.getParcelable(ALBUM)

        viewModel.liveData.observe(this) {
            when (it.response) {
                Resource.Empty -> {
                }
                Resource.Loading -> {
                }
                is Resource.Error -> {
                    Timber.w(it.response.message)
                    Toast.makeText(activity, "Error loading Tracks", Toast.LENGTH_LONG).show()
                }
                is Resource.Success -> {
                    adapter.replace(it.response.data.tracks)
                }
            }
        }

        adapter = TracksAdapter(null)
        viewModel.loadDetail(albumSimple.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_album_detail, container, false)

        rootView.album_detail.text = resources.getString(R.string.by_author, albumSimple.artist).toUpperCase()
        rootView.album_detail_list.adapter = adapter

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.toolbar_layout?.title = albumSimple.title
        Glide.with(this)
                .load(albumSimple.cover)
                .into(activity?.cover!!)
    }

    companion object {
        const val ALBUM = "album"
    }
}
