package com.denisbrandi.githubprojects.data.mapper

import com.denisbrandi.githubprojects.data.model.JsonGithubProject
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GithubProjectMapperTest {

    private val sut = GithubProjectMapper

    @Test
    fun `map SHOULD map provided values`() {
        val input = JsonGithubProject("id", "name", "desc")

        val result = sut.map(input)

        with(result) {
            assertThat(id).isEqualTo("id")
            assertThat(name).isEqualTo("name")
            assertThat(description).isEqualTo("desc")
        }
    }

    @Test
    fun `map SHOULD map null values to defaults`() {
        val input = JsonGithubProject("id")

        val result = sut.map(input)

        with(result) {
            assertThat(id).isEqualTo("id")
            assertThat(name).isEmpty()
            assertThat(description).isEmpty()
        }
    }
}