package net.marcorighini.spotifyreleases.misc.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

typealias UiAction = (Fragment) -> Unit

class UiActionsLiveData {
    private var list: MutableList<UiAction> = ArrayList()
    private val delegate = MutableLiveData<List<UiAction>>()

    private fun execute(action: UiAction) {
        list.add(action)
        delegate.value = list
    }

    operator fun invoke(action: UiAction) {
        execute(action)
    }

    fun observe(owner: LifecycleOwner, executor: (UiAction) -> Unit) {
        delegate.observe(owner, Observer {
            list.forEach(executor)
            list = ArrayList()
        })
    }
}