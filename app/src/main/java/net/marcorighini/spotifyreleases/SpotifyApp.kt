package net.marcorighini.spotifyreleases

import android.app.Application
import android.content.Context
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import net.marcorighini.spotifyreleases.misc.AppComponent
import net.marcorighini.spotifyreleases.misc.DaggerAppComponent
import timber.log.Timber


class SpotifyApp : Application() {
    @set:VisibleForTesting
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        component = DaggerAppComponent.builder().application(this).build()
    }
}

val Context.component: AppComponent
    get() = (applicationContext as SpotifyApp).component

val Fragment.component: AppComponent
    get() = activity!!.component