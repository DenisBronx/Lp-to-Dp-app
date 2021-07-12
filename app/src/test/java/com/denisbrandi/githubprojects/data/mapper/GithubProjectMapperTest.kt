package com.denisbrandi.githubprojects.data.mapper

import com.denisbrandi.githubprojects.data.model.*
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GithubProjectMapperTest {

    private val sut = GithubProjectMapper

    @Test
    fun `map SHOULD map provided values`() {
        val input =
            JsonGithubProject(
                id = "id",
                name = "name",
                fullName = "fullName",
                description = "desc",
                owner = JsonOwner("imageUrl")
            )

        val result = sut.map(input)

        with(result) {
            assertThat(id).isEqualTo("id")
            assertThat(name).isEqualTo("name")
            assertThat(fullName).isEqualTo("fullName")
            assertThat(description).isEqualTo("desc")
            assertThat(imageUrl).isEqualTo("imageUrl")
        }
    }

    @Test
    fun `map SHOULD map null values to defaults`() {
        val input = JsonGithubProject("id")

        val result = sut.map(input)

        with(result) {
            assertThat(id).isEqualTo("id")
            assertThat(name).isEmpty()
            assertThat(fullName).isEmpty()
            assertThat(description).isEmpty()
            assertThat(imageUrl).isEmpty()
        }
    }
}