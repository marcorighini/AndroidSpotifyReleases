package net.marcorighini.spotifyreleases.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.album_list_content.view.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple


class AlbumListAdapter(
        private val context: Context,
        private var albums: MutableList<AlbumSimple>?,
        private var favourites: MutableList<AlbumSimple>?,
        private val clickListener: AlbumClickListener,
        private val favouriteToggleListener: AlbumFavouriteToggleListener
) :
        RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {

    interface AlbumClickListener {
        fun onAlbumClick(album: AlbumSimple)
    }

    interface AlbumFavouriteToggleListener {
        fun onAlbumFavouriteToggle(album: AlbumSimple)
    }

    fun replace(a: List<AlbumSimple>?,
            f: List<AlbumSimple>) {
        a?.let {
            albums = a.toMutableList()
            favourites = f.toMutableList()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.album_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favIds = favourites?.map { it.id }
        val item = albums?.get(position)
        with(holder) {
            title.text = item?.title
            artist.text = item?.artist

            if (favIds != null && item != null) {
                if (favIds.contains(item.id)) {
                    Glide.with(context)
                            .load(R.drawable.fav)
                            .into(favourite)
                } else {
                    Glide.with(context)
                            .load(R.drawable.fav_not)
                            .into(favourite)
                }
            }

            Glide.with(context)
                    .load(item?.cover)
                    .into(cover)

            itemView.setOnClickListener {
                if (item != null) {
                    clickListener.onAlbumClick(item)
                }
            }

            favourite.setOnClickListener {
                if (item != null) {
                    favouriteToggleListener.onAlbumFavouriteToggle(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return albums?.size ?: 0
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val title: TextView = mView.title
        val artist: TextView = mView.artist
        val cover: ImageView = mView.cover
        val favourite: ImageView = mView.fav
    }
}