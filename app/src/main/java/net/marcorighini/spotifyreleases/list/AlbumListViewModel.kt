package net.marcorighini.spotifyreleases.list

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import net.marcorighini.spotifyreleases.misc.NavigationController
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.UiActionsLiveData
import javax.inject.Inject


class AlbumListViewModel @Inject constructor(
        private val preferences: PreferencesService,
        private val navigationController: NavigationController,
        private val newReleasesRepository: NewReleasesRepository
) : ViewModel() {

    val uiActions = UiActionsLiveData()
    val stateLiveData = MediatorLiveData<AlbumListViewState>()

    private val job = Job()

    init {
        stateLiveData.value = AlbumListViewState(Resource.Empty, mutableListOf())
        stateLiveData.addSource(newReleasesRepository.releases) {
            stateLiveData.value = stateLiveData.value!!.copy(
                    response = it?.map { it.items.map { AlbumSimple(it.id, it.images[0].url, it.name, it.artists[0].name) } }!!,
                    favourites = preferences.favourites
            ) ?: stateLiveData.value
        }
        refresh()
    }

    fun refresh() {
        launch(UI, parent = job) {
            newReleasesRepository.refresh()
        }
    }

    fun onAlbumClick(album: AlbumSimple) {
        uiActions { navigationController.navigateToDetail(it, album) }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
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

        stateLiveData.value = stateLiveData.value?.copy(favourites = preferences.favourites) ?: stateLiveData.value
    }
}