package net.marcorighini.spotifyreleases.misc

import android.app.Application
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import net.marcorighini.spotifyreleases.BuildConfig
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.services.SpotifyService
import net.marcorighini.spotifyreleases.misc.utils.createService
import okhttp3.HttpUrl
import javax.inject.Singleton


@Module
class AppModule {
    @Provides
    @Singleton
    fun provideSpotifyService() = createService<SpotifyService>(BuildConfig.DEBUG, HttpUrl.parse("https://api.spotify.com/v1/")!!)

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application) = PreferencesService(PreferenceManager.getDefaultSharedPreferences(application))
}