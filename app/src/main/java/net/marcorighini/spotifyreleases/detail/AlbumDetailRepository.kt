package net.marcorighini.spotifyreleases.detail

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.services.SpotifyService
import net.marcorighini.spotifyreleases.misc.services.SpotifyServiceModel
import net.marcorighini.spotifyreleases.misc.utils.Resource
import javax.inject.Inject


class AlbumDetailRepository @Inject constructor(
        private val api: SpotifyService,
        private val preferencesService: PreferencesService
) {
    private val subject = PublishSubject.create<Resource<SpotifyServiceModel.AlbumDetail>>()
    fun albumDetail(): Observable<Resource<SpotifyServiceModel.AlbumDetail>> = subject
    private var resource: Resource<SpotifyServiceModel.AlbumDetail> = Resource.Empty

    suspend fun refresh(id: String) {
        if (resource != Resource.Loading) {
            resource = Resource.Loading
            subject.onNext(resource)
            withContext(CommonPool) {
                try {
                    val albums = api.getAlbum("Bearer " + preferencesService.loginAccessToken, id).await()
                    resource = Resource.Success(albums)
                    subject.onNext(resource)
                } catch (e: Exception) {
                    resource = Resource.Error(e)
                    subject.onNext(resource)
                }
            }
        }
    }
}

