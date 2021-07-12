package com.denisbrandi.githubprojects.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

open class BaseViewModel<S>(initialState: S) : ViewModel() {

    private val _state = MutableStateFlow(initialState)

    val state = _state.asStateFlow()

    fun setState(newState: S) {
        _state.value = newState
    }

}