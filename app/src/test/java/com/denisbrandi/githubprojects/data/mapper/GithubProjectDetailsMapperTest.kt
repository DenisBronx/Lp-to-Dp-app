package com.denisbrandi.githubprojects.data.mapper

import com.denisbrandi.githubprojects.data.model.*
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GithubProjectDetailsMapperTest {

    private val sut = GithubProjectDetailsMapper

    @Test
    fun `EXPECT values properly mapped WHEN there are no nulls`() {
        val input = JsonGithubProjectDetails(
            id = "id",
            name = "name",
            fullName = "fullName",
            description = "desc",
            owner = JsonOwner("imageUrl"),
            url = "url",
            stargazers = 5,
            watchers = 6
        )

        val result = sut.map(input)

        with(result) {
            assertThat(id).isEqualTo("id")
            assertThat(name).isEqualTo("name")
            assertThat(fullName).isEqualTo("fullName")
            assertThat(description).isEqualTo("desc")
            assertThat(imageUrl).isEqualTo("imageUrl")
            assertThat(url).isEqualTo("url")
            assertThat(stargazers).isEqualTo(5)
            assertThat(watchers).isEqualTo(6)
        }
    }

    @Test
    fun `EXPECT default values WHEN fields are null`() {
        val input = JsonGithubProjectDetails("id")

        val result = sut.map(input)

        with(result) {
            assertThat(id).isEqualTo("id")
            assertThat(name).isEmpty()
            assertThat(fullName).isEmpty()
            assertThat(description).isEmpty()
            assertThat(imageUrl).isEmpty()
            assertThat(url).isEmpty()
            assertThat(stargazers).isEqualTo(0)
            assertThat(watchers).isEqualTo(0)
        }
    }
}