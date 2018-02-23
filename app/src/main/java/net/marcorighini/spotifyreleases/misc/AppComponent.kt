package net.marcorighini.spotifyreleases.misc

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import net.marcorighini.spotifyreleases.detail.AlbumDetailViewModel
import net.marcorighini.spotifyreleases.favourites.FavouritesListViewModel
import net.marcorighini.spotifyreleases.list.AlbumListViewModel
import net.marcorighini.spotifyreleases.login.LoginViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun loginViewModel(): LoginViewModel
    fun listViewModel(): AlbumListViewModel
    fun detailViewModel(): AlbumDetailViewModel
    fun favouriteViewModel(): FavouritesListViewModel

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}