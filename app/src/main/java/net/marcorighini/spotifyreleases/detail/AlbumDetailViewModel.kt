package net.marcorighini.spotifyreleases.detail

import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import net.marcorighini.spotifyreleases.misc.model.AlbumDetail
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.model.Track
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.services.SpotifyService
import net.marcorighini.spotifyreleases.misc.utils.LiveDataDelegate
import net.marcorighini.spotifyreleases.misc.utils.Resource
import javax.inject.Inject


class AlbumDetailViewModel @Inject constructor(
        private val preferences: PreferencesService,
        private val albumDetailRepository: AlbumDetailRepository
) : ViewModel() {

    val liveData = LiveDataDelegate(AlbumDetailViewState(Resource.Empty, preferences.favourites))
    private var state by liveData

    private val job = Job()
    private val disposable = CompositeDisposable()

    init {
        disposable.add(
                albumDetailRepository.albumDetail()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { e ->
                            state = state.copy(
                                    response = e.map {
                                        AlbumDetail(it.id, it.images[0].url, it.name, it.artists[0].name, it.tracks.items.map {
                                            Track(it.name, it.duration_ms, it.track_number)
                                        })
                                    },
                                    favourites = preferences.favourites
                            )
                        }
        )
    }

    fun loadDetail(id: String) {
        launch(parent = job) {
            albumDetailRepository.refresh(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable.clear()
    }
}