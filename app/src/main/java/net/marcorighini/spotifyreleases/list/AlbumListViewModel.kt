package net.marcorighini.spotifyreleases.list

import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import net.marcorighini.spotifyreleases.misc.NavigationController
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.services.SpotifyService
import net.marcorighini.spotifyreleases.misc.utils.LiveDataDelegate
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.UiActionsLiveData
import javax.inject.Inject


class AlbumListViewModel @Inject constructor(
        private val preferences: PreferencesService,
        private val navigationController: NavigationController,
        private val spotifyService: SpotifyService
) : ViewModel() {
    companion object {
        const val RELEASE_LIMIT = 50
    }

    val uiActions = UiActionsLiveData()
    val liveData = LiveDataDelegate(AlbumListViewState(Resource.Empty, preferences.favourites))
    private var state by liveData

    private val disposable = CompositeDisposable()

    init {
        refresh()
    }

    fun refresh() {
        if (state.response != Resource.Loading) {
            state = state.copy(response = Resource.Loading)
            disposable.add(spotifyService.getNewReleases("Bearer " + preferences.loginAccessToken, RELEASE_LIMIT)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onSuccess = {
                                val l = it.albums.items.map {
                                    AlbumSimple(it.id, it.images[0].url, it.name, it.artists[0].name)
                                }
                                state = state.copy(response = Resource.Success(l), favourites = preferences.favourites)
                            },
                            onError = {
                                state = state.copy(response = Resource.Error(it), favourites = preferences.favourites)
                            }
                    )
            )
        }
    }

    fun onAlbumClick(album: AlbumSimple) {
        uiActions { navigationController.navigateToDetail(it, album) }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
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

        if (state.response is Resource.Success) {
            state = state.copy(favourites = preferences.favourites)
        }
    }

    fun onFavouriteClick() {
        uiActions { navigationController.navigateToFavourites(it) }
    }
}