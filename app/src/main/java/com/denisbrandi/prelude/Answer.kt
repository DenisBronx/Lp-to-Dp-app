package com.denisbrandi.prelude

sealed class Answer<out T, out E> {

    data class Success<out T>(val data: T) : Answer<T, Nothing>()

    data class Error<out E>(val reason: E) : Answer<Nothing, E>()

    inline fun <C> fold(success: (T) -> C, error: (E) -> C): C = when (this) {
        is Success -> success(data)
        is Error -> error(reason)
    }

    inline fun <NewT : Any> map(mapper: (T) -> NewT): Answer<NewT, E> {
        return fold(success = { Success(mapper(it)) }, error = { Error(it) })
    }

    fun asSuccess() = this as Success<T>

    fun asError() = this as Error<E>

    fun isSuccess() = this is Success<T>

    fun isError() = this is Error<E>
}