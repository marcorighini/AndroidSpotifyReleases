package net.marcorighini.spotifyreleases.misc

import android.support.v4.app.Fragment
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.detail.AlbumDetailFragment
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class NavigationController @Inject constructor() {
    fun navigateToList(fragment: Fragment){
        fragment.findNavController().navigate(R.id.action_loginFragment_to_albumListFragment)
    }

    fun navigateToDetail(fragment: Fragment, album: AlbumSimple){
        val bundle = bundleOf(AlbumDetailFragment.ALBUM to album)
        fragment.findNavController().navigate(R.id.albumDetailFragment, bundle)
    }

    fun navigateToFavourites(fragment: Fragment) {
        fragment.findNavController().navigate(R.id.favouritesListFragment)
    }
}