package net.marcorighini.spotifyreleases.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.album_detail_list_content.view.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.misc.model.Track
import java.util.concurrent.TimeUnit


class TracksAdapter(
        private var tracks: List<Track>?
) :
        RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    fun replace(albums: List<Track>?) {
        albums?.let {
            this.tracks = albums
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_detail_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tracks?.get(position)
        holder.number.text = item?.number.toString()
        holder.title.text = item?.title
        val millis = item?.durationMs!!.toLong()
        holder.duration.text = String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
        with(holder.itemView) {
            tag = item
        }
    }

    override fun getItemCount(): Int {
        return tracks?.size ?: 0
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val number: TextView = mView.trackNumber
        val title: TextView = mView.trackTitle
        val duration: TextView = mView.trackDuration
    }
}