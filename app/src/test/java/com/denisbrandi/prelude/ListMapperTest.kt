package com.denisbrandi.prelude

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.kotlin.*

class ListMapperTest {

    private companion object {
        val input1 = Any()
        val input2 = Any()
        val output1 = Any()
        val output2 = Any()
    }

    private val objectMapper: (input: Any) -> Any = mock {
        on { invoke(input1) } doReturn output1
        on { invoke(input2) } doReturn output2
    }

    private val sut = ListMapper(objectMapper)

    @Test
    fun `map SHOULD return empty list WHEN input list is empty`() {
        val result = sut.map(emptyList())

        assertThat(result).isEmpty()
    }

    @Test
    fun `map SHOULD return list of 1 WHEN input list has 1 item`() {
        val result = sut.map(listOf(input1))

        assertThat(result).isEqualTo(listOf(output1))
    }

    @Test
    fun `map SHOULD return list of 2 WHEN input list has 2 items`() {
        val result = sut.map(listOf(input1, input2))

        assertThat(result).isEqualTo(listOf(output1, output2))
    }
}