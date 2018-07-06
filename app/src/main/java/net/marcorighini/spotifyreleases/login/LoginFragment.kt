package net.marcorighini.spotifyreleases.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.spotify.sdk.android.authentication.AuthenticationClient
import kotlinx.android.synthetic.main.fragment_login.*
import net.marcorighini.spotifyreleases.R
import net.marcorighini.spotifyreleases.component
import net.marcorighini.spotifyreleases.misc.utils.Resource
import net.marcorighini.spotifyreleases.misc.utils.viewModelProvider


class LoginFragment : Fragment() {
    private val viewModel by viewModelProvider { component.loginViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        login.setOnClickListener {
            viewModel.requestAuth(this)
        }

        viewModel.uiActions.observe(this) { it(this) }
        viewModel.liveData.observe(this) {
            when (it.response) {
                Resource.Empty -> {
                }
                Resource.Loading -> {
                }
                is Resource.Success -> {
                }
                is Resource.Error -> {
                    Toast.makeText(activity!!, "Error logging in", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onAuthenticationResult(AuthenticationClient.getResponse(resultCode, data))
    }
}