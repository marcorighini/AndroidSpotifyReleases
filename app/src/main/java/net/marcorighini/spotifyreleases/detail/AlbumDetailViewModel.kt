package net.marcorighini.spotifyreleases.detail

import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import net.marcorighini.spotifyreleases.misc.model.AlbumDetail
import net.marcorighini.spotifyreleases.misc.model.Track
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.services.SpotifyService
import net.marcorighini.spotifyreleases.misc.utils.LiveDataDelegate
import net.marcorighini.spotifyreleases.misc.utils.Resource
import javax.inject.Inject


class AlbumDetailViewModel @Inject constructor(
        private val preferences: PreferencesService,
        private val spotifyService: SpotifyService
) : ViewModel() {

    val liveData = LiveDataDelegate(AlbumDetailViewState(Resource.Empty, preferences.favourites))
    private var state by liveData

    private val disposable = CompositeDisposable()

    fun loadDetail(id: String) {
        if (state.response == Resource.Empty || state.response is Resource.Error) {
            state = state.copy(response = Resource.Loading)
            disposable.add(spotifyService.getAlbum("Bearer " + preferences.loginAccessToken, id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onSuccess = {
                                val d = AlbumDetail(it.id, it.images[0].url, it.name, it.artists[0].name, it.tracks.items.map {
                                    Track(it.name, it.duration_ms, it.track_number)
                                })
                                state = state.copy(response = Resource.Success(d))
                            },
                            onError = {
                                state = state.copy(response = Resource.Error(it))
                            }
                    )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}