package net.marcorighini.spotifyreleases.list

import net.marcorighini.spotifyreleases.misc.model.AlbumSimple
import net.marcorighini.spotifyreleases.misc.utils.Resource

data class AlbumListViewState(val response: Resource<List<AlbumSimple>>, val favourites: List<AlbumSimple>)


