package com.denisbrandi.prelude

class ListMapper<I,O>(private val objectMapper: (input: I) -> O) {

    fun map(inputList: List<I>): List<O> {
        return inputList.map { objectMapper(it) }
    }

}