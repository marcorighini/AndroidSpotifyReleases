package net.marcorighini.spotifyreleases.login

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE
import net.marcorighini.spotifyreleases.misc.NavigationController
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.misc.services.PreferencesService
import net.marcorighini.spotifyreleases.misc.utils.LiveDataDelegate
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.UiActionsLiveData
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class LoginViewModel @Inject constructor(
        private val preferences: PreferencesService,
        private val navigationController: NavigationController
) : ViewModel() {

    val uiActions = UiActionsLiveData()
    val liveData = LiveDataDelegate(LoginViewState(Resource.Empty))
    private var state by liveData

    init {
        if (loginExpired()) {
            Timber.d("Login Expired")
            state.copy(response = Resource.Empty)
        } else {
            Timber.d("Login Not Expired")
            state.copy(response = Resource.Success(preferences.loginAccessToken))
            uiActions { navigationController.navigateToList(it) }
        }
    }

    private fun loginExpired(): Boolean {
        val now = Date().time
        return now >= preferences.loginExpiration
    }

    fun requestAuth(fragment: Fragment) {
        state.copy(response = Resource.Loading)
        val builder = AuthenticationRequest.Builder(fragment.getString(R.string.spotify_client_id), AuthenticationResponse.Type.TOKEN,
                fragment.getString(R.string.com_spotify_sdk_redirect_scheme) + "://" + fragment.getString(R.string.com_spotify_sdk_redirect_host))
        val intent = AuthenticationClient.createLoginActivityIntent(fragment.activity, builder.build())
        fragment.startActivityForResult(intent, REQUEST_CODE)
    }

    fun onAuthenticationResult(res: AuthenticationResponse) {
        when (res.type) {
            AuthenticationResponse.Type.TOKEN -> {
                Timber.d("Login success")
                preferences.loginAccessToken = res.accessToken
                preferences.loginExpiration = Date().time + res.expiresIn * 1000
                state.copy(response = Resource.Success(res.accessToken))
                uiActions { navigationController.navigateToList(it) }
            }
            else -> {
                Timber.w("Login error")
                state.copy(response = Resource.Error(res.toString()))
            }
        }
    }
}