package net.marcorighini.spotifyreleases.detail

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
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

    private val job = Job()

    fun loadDetail(id: String) {
        if (state.response == Resource.Empty || state.response is Resource.Error) {
            state = state.copy(response = Resource.Loading)
            launch(CommonPool + job) {
                try {
                    val res = spotifyService.getAlbum("Bearer " + preferences.loginAccessToken, id).await()
                    val d = AlbumDetail(res.id, res.images[0].url, res.name, res.artists[0].name, res.tracks.items.map {
                        Track(it.name, it.duration_ms, it.track_number)
                    })
                    withContext(UI) {
                        state = state.copy(response = Resource.Success(d))
                    }
                } catch (e: Exception) {
                    withContext(UI) {
                        state = state.copy(response = Resource.Error(e))
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}