package com.denisbrandi.testutil

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher

class FlowTestObserver<T>(private val flow: Flow<T>, coroutineScope: CoroutineScope) {
    private val emittedValues = mutableListOf<T>()
    private val job: Job = flow.onEach {
        emittedValues.add(it)
    }.launchIn(coroutineScope)

    fun assertEmpty() {
        assertThat(emittedValues).isEmpty()
    }

    fun assertValues(vararg values: T) {
        assertSize(values.size)
        assertSequence(values.toList())
    }

    fun assertSize(size: Int) {
        assertThat(emittedValues.size).isEqualTo(size)
    }

    fun getValues() = emittedValues

    fun stopObserving() {
        job.cancel()
    }

    fun getFlow() = flow

    private fun assertSequence(values: List<T>) {
        for ((index, v) in values.withIndex()) {
            assertThat(emittedValues[index]).isEqualTo(v)
        }
    }
}

fun <T> Flow<T>.test(coroutineScope: CoroutineScope = CoroutineScope(TestCoroutineDispatcher())) =
    FlowTestObserver(this, coroutineScope)