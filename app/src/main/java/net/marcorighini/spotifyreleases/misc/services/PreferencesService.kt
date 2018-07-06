package net.marcorighini.spotifyreleases.misc.services

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.marcorighini.spotifyreleases.misc.model.AlbumSimple

class PreferencesService(private val prefs: SharedPreferences) {
    companion object {
        const val EXPIRATION = "loginExpiration"
        const val ACCESS_TOKEN = "accesstoken"
        const val FAVOURITES = "favourites"
    }

    var loginAccessToken: String
        get() = prefs.getString(ACCESS_TOKEN, "")
        set(value) {
            prefs.edit {
                putString(ACCESS_TOKEN, value)
            }
        }

    var loginExpiration: Long
        get() = prefs.getLong(EXPIRATION, 0)
        set(value) {
            prefs.edit {
                putLong(EXPIRATION, value)
            }
        }
    var favourites: List<AlbumSimple>
        get() {
            val elems = prefs.getString(FAVOURITES, "")
            if (elems.isNotEmpty()) {
                val listType = object : TypeToken<ArrayList<AlbumSimple>>() {}.type
                return Gson().fromJson<List<AlbumSimple>>(elems, listType)
            }
            return ArrayList()
        }
        set(value) {
            prefs.edit {
                putString(FAVOURITES, Gson().toJson(value))
            }
        }
}