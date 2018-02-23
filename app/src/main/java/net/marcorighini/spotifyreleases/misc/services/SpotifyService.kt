package net.marcorighini.spotifyreleases.misc.services

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface SpotifyService {
    @GET("browse/new-releases")
    fun getNewReleases(@Header("Authorization") auth: String, @Query("limit") limit: Int): Single<SpotifyServiceModel.NewReleasesResponse>

    @GET("albums/{id}")
    fun getAlbum(@Header("Authorization") auth: String, @Path("id") id: String): Single<SpotifyServiceModel.AlbumDetail>
}

object SpotifyServiceModel {
    data class Artist(val external_urls: ExternalUrls, val href: String, val id: String, val name: String, val type: String, val uri: String)
    data class ExternalUrls(val spotify: String)
    data class Image(val height: Int, val url: String, val width: Int)
    data class Copyright(val text: String, val type: String)
    data class ExternalIds(val upc: String)

    data class NewReleasesResponse(val albums: AlbumsSimple)
    data class AlbumsSimple(val href: String, val items: List<AlbumsSimpleItem>, val limit: Int, val next: String, val offset: Int,
            val previous: String,
            val total: Int)

    data class AlbumsSimpleItem(val album_type: String, val artists: List<Artist>, val available_markets: List<String>,
            val external_urls: ExternalUrls, val href: String, val id: String, val images: List<Image>, val name: String, val release_date: String,
            val release_date_precision: String, val type: String, val uri: String)

    data class AlbumDetail(val album_type: String, val artists: List<Artist>, val available_markets: List<String>, val copyrights: List<Copyright>,
            val external_ids: ExternalIds, val external_urls: ExternalUrls, val genres: List<String>, val href: String, val id: String,
            val images: List<Image>, val label: String, val name: String, val popularity: Int, val release_date: String,
            val release_date_precision: String, val tracks: Tracks, val type: String, val uri: String)

    data class Tracks(val href: String, val items: List<TracksItem>, val limit: Int, var next: String, var offset: Int, var previous: String,
            var total: Int)

    data class TracksItem(val artists: List<Artist>, val available_markets: List<String>, val discNumber: Int, val duration_ms: Int,
            val explicit: Boolean, val external_urls: ExternalUrls, val href: String, val id: String, val name: String, val preview_url: String,
            val track_number: Int, val type: String, val uri: String)

}