package net.marcorighini.spotifyreleases.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.spotify.sdk.android.authentication.AuthenticationClient
import kotlinx.android.synthetic.main.activity_login.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.component
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.viewModelProvider


class LoginActivity : AppCompatActivity() {


    private val viewModel by viewModelProvider { component.loginViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login.setOnClickListener {
            viewModel.requestAuth(this)
        }

        viewModel.uiActions.observe(this) { it(this) }
        viewModel.liveData.observe(this) {
            when (it.response) {
                Resource.Empty -> { // TODO show login button
                }
                Resource.Loading -> { // TODO show loading wheel
                }
                is Resource.Success -> {
                }
                is Resource.Error -> { // TODO show toast
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onAuthenticationResult(AuthenticationClient.getResponse(resultCode, data))
    }
}