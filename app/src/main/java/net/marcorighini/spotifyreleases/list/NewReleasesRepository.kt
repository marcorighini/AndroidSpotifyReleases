package net.marcorighini.spotifyreleases.list

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.services.SpotifyService
import net.marcorighini.spotifyreleases.misc.services.SpotifyServiceModel
import net.marcorighini.spotifyreleases.misc.utils.Resource
import javax.inject.Inject

class NewReleasesRepository @Inject constructor(
        private val api: SpotifyService,
        private val preferencesService: PreferencesService
) {
    val releases = MutableLiveData<Resource<SpotifyServiceModel.AlbumsSimple>>()
    private var resource: Resource<SpotifyServiceModel.AlbumsSimple>

    init {
        resource = Resource.Empty
        releases.value = resource
    }

    suspend fun refresh() {
        if (resource != Resource.Loading) {
            resource = Resource.Loading
            releases.value = resource
            withContext(CommonPool) {
                resource = try {
                    val albums = api.getNewReleases("Bearer " + preferencesService.loginAccessToken, RELEASE_LIMIT).await().albums
                    Resource.Success(albums)
                } catch (e: Exception) {
                    Resource.Error(e)
                }
            }
            releases.value = resource
        }
    }

    companion object {
        const val RELEASE_LIMIT = 50
    }
}
