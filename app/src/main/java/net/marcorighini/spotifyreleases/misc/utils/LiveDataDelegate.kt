package net.marcorighini.spotifyreleases.misc.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import kotlin.reflect.KProperty


class LiveDataDelegate<T>(
        initialState: T
) {
    private val liveData = MutableLiveData<T>()

    init {
        liveData.value = initialState
    }

    fun observe(owner: LifecycleOwner, observer: (T) ->Unit){
        liveData.observe(owner, Observer { observer(it!!) })
    }

    fun observeForever(owner: LifecycleOwner, observer: (T) ->Unit){
        liveData.observeForever { observer(it!!) }
    }

    operator fun setValue(ref: Any, p: KProperty<*>, value: T) {
        liveData.value = value
    }

    operator fun getValue(ref: Any, p: KProperty<*>): T =
            liveData.value!!
}