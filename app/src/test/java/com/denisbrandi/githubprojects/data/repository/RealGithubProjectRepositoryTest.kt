package com.denisbrandi.githubprojects.data.repository

import com.denisbrandi.githubprojects.data.model.JsonGithubProject
import com.denisbrandi.githubprojects.data.remote.GithubProjectApiService
import com.denisbrandi.githubprojects.domain.model.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class RealGithubProjectRepositoryTest {

    private companion object {
        const val OWNER = "square"

        val githubProjectsDTO = listOf(JsonGithubProject(id = "1"), JsonGithubProject(id = "2"))
        val githubProjects = listOf(GithubProject(id = "1"), GithubProject(id = "2"))
    }

    private val githubProjectApiService: GithubProjectApiService = mock()
    private val githubProjectsMapper: (jsonProjects: List<JsonGithubProject>) -> List<GithubProject> =
            mock {
                on { invoke(githubProjectsDTO) } doReturn githubProjects
            }
    private val sut = RealGithubProjectRepository(githubProjectApiService, githubProjectsMapper)

    @Test
    fun `getProjectsForOrganisation SHOULD return mapped result WHEN api is successful`() =
            runBlockingTest {
                whenever(githubProjectApiService.getProjectsForOrganisation(OWNER))
                        .doReturn(Response.success(githubProjectsDTO))

                val result = sut.getProjectsForOrganisation(OWNER)

                assertThat(result.asSuccess().data).isEqualTo(githubProjects)
            }

    @Test
    fun `getProjectsForOrganisation SHOULD return NoProjectFound WHEN api is successful but body is null`() =
            runBlockingTest {
                whenever(githubProjectApiService.getProjectsForOrganisation(OWNER))
                        .doReturn(Response.success(null))

                val result = sut.getProjectsForOrganisation(OWNER)

                assertThat(result.asError().reason).isEqualTo(GetProjectsError.NoProjectFound)
            }

    @Test
    fun `getProjectsForOrganisation SHOULD return NoProjectFound WHEN api is not successful because of 404`() =
            runBlockingTest {
                whenever(githubProjectApiService.getProjectsForOrganisation(OWNER))
                        .doReturn(Response.error(404, ResponseBody.create(null, "")))

                val result = sut.getProjectsForOrganisation(OWNER)

                assertThat(result.asError().reason).isEqualTo(GetProjectsError.NoProjectFound)
            }

    @Test
    fun `getProjectsForOrganisation SHOULD return GenericError WHEN api is not successful because of generic error`() =
            runBlockingTest {
                whenever(githubProjectApiService.getProjectsForOrganisation(OWNER))
                        .doReturn(Response.error(500, ResponseBody.create(null, "")))

                val result = sut.getProjectsForOrganisation(OWNER)

                assertThat(result.asError().reason).isEqualTo(GetProjectsError.GenericError)
            }
}