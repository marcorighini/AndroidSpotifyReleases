package net.marcorighini.spotifyreleases.list

import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import net.marcorighini.spotifyreleases.misc.NavigationController
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.utils.LiveDataDelegate
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.UiActionsLiveData
import javax.inject.Inject


class AlbumListViewModel @Inject constructor(
        private val preferences: PreferencesService,
        private val navigationController: NavigationController,
        private val newReleasesRepository: NewReleasesRepository
) : ViewModel() {


    val uiActions = UiActionsLiveData()
    val liveData = LiveDataDelegate(AlbumListViewState(Resource.Empty, preferences.favourites))
    private var state by liveData

    private val job = Job()
    private val disposable = CompositeDisposable()

    init {
        disposable.add(
                newReleasesRepository.releases()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { e ->
                            state = state.copy(
                                    response = e.map { it.items.map { AlbumSimple(it.id, it.images[0].url, it.name, it.artists[0].name) } },
                                    favourites = preferences.favourites
                            )
                        }
        )
        refresh()
    }

    fun refresh() {
        launch(parent = job) {
            newReleasesRepository.refresh()
        }
    }

    fun onAlbumClick(album: AlbumSimple) {
        uiActions { navigationController.navigateToDetail(it, album) }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
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