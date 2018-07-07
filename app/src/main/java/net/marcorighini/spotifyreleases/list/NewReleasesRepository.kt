package net.marcorighini.spotifyreleases.list

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
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
    private val subject = PublishSubject.create<Resource<SpotifyServiceModel.AlbumsSimple>>()
    fun releases(): Observable<Resource<SpotifyServiceModel.AlbumsSimple>> = subject
    private var resource: Resource<SpotifyServiceModel.AlbumsSimple> = Resource.Empty

    suspend fun refresh() {
        if (resource != Resource.Loading) {
            resource = Resource.Loading
            subject.onNext(resource)
            withContext(CommonPool) {
                try {
                    val albums = api.getNewReleases("Bearer " + preferencesService.loginAccessToken, RELEASE_LIMIT).await().albums
                    resource = Resource.Success(albums)
                    subject.onNext(resource)
                } catch (e: Exception) {
                    resource = Resource.Error(e)
                    subject.onNext(resource)
                }
            }
        }
    }

    companion object {
        const val RELEASE_LIMIT = 50
    }
}
