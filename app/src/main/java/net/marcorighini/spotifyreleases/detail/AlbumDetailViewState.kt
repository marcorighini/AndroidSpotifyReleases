package net.marcorighini.spotifyreleases.detail

import net.marcorighini.spotifyreleases.misc.model.AlbumDetail
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.utils.Resource

data class AlbumDetailViewState(val response: Resource<AlbumDetail>, val favourites: List<AlbumSimple>)



