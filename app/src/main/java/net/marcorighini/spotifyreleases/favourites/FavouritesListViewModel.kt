package net.marcorighini.spotifyreleases.favourites

import android.arch.lifecycle.ViewModel
import net.marcorighini.spotifyreleases.misc.NavigationController
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.utils.LiveDataDelegate
import net.marcorighini.spotifyreleases.misc.utils.UiActionsLiveData
import javax.inject.Inject


class FavouritesListViewModel @Inject constructor(
        private val preferences: PreferencesService,
        private val navigationController: NavigationController
) : ViewModel() {

    val uiActions = UiActionsLiveData()
    val liveData = LiveDataDelegate(FavouritesListViewState(ArrayList()))
    private var state by liveData

    init {
        state = state.copy(favourites = preferences.favourites)

    }

    fun onAlbumClick(album: AlbumSimple) {
        uiActions { navigationController.navigateToDetail(it, album) }
    }

    fun onFavouriteToggle(album: AlbumSimple) {
        val prefs = preferences.favourites.toMutableList()
        val idx = prefs.indexOfFirst { it.id == album.id }
        if (idx != -1) {
            prefs.removeAt(idx)
        } else {
            prefs.add(album)
        }
        preferences.favourites = prefs
        state = state.copy(favourites = preferences.favourites)
    }
}