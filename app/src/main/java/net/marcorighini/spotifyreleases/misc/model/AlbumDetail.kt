package net.marcorighini.spotifyreleases.misc.model

data class AlbumDetail(val id: String, val cover: String, val title: String, val artist: String, val tracks: List<Track>)