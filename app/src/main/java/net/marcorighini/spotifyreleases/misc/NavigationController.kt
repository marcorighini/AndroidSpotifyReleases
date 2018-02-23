package net.marcorighini.spotifyreleases.misc

import android.content.Intent
import android.support.v4.app.FragmentActivity
import net.marcorighini.spotifyreleases.detail.AlbumDetailActivity
import net.marcorighini.spotifyreleases.detail.AlbumDetailFragment
import net.marcorighini.spotifyreleases.favourites.FavouritesListActivity
import net.marcorighini.spotifyreleases.list.AlbumListActivity
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class NavigationController @Inject constructor() {
    fun navigateToList(activity: FragmentActivity){
        val i = Intent(activity, AlbumListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        activity.startActivity(i)
        activity.finish()
    }

    fun navigateToDetail(activity: FragmentActivity, album: AlbumSimple){
        val i = Intent(activity, AlbumDetailActivity::class.java).apply {
            putExtra(AlbumDetailFragment.ALBUM, album)
        }
        activity.startActivity(i)
    }

    fun navigateToFavourites(activity: FragmentActivity) {
        activity.startActivity(Intent(activity, FavouritesListActivity::class.java))
    }
}