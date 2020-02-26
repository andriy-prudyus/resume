package com.andriiprudyus.myresume.base.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class ResultObserver<T : Any>(
    private val liveData: LiveData<State<T>>,
    private val action: (state: State<T>) -> Unit
) : Observer<State<T>> {

    override fun onChanged(state: State<T>?) {
        when (state) {
            is State.Loading -> action(state)
            is State.Success -> action(state).also { liveData.removeObserver(this) }
            is State.Failure -> action(state).also { liveData.removeObserver(this) }
        }
    }
}