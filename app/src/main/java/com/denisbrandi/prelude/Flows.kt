package com.denisbrandi.prelude

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*

inline fun <T : Any> Flow<T>.flowWhenStarted(
    lifecycleOwner: LifecycleOwner, crossinline collector: (T) -> Unit
) {
    flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
        .onEach { collector(it) }
        .launchIn(lifecycleOwner.lifecycleScope)
}