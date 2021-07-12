package com.denisbrandi.githubprojects.data.repository

import com.denisbrandi.githubprojects.data.model.JsonGithubProject
import com.denisbrandi.githubprojects.data.remote.GithubProjectApiService
import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.testutil.stubOrThrow
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class RealGithubProjectRepositoryTest {

    private companion object {
        const val ORGANISATION = "square"

        val githubProjectsDTO = listOf(JsonGithubProject(id = "1"), JsonGithubProject(id = "2"))
        val githubProjects = listOf(GithubProject(id = "1"), GithubProject(id = "2"))
    }

    private val fakeGithubProjectApiService = FakeGithubProjectApiService()
    private val sut = RealGithubProjectRepository(
        fakeGithubProjectApiService,
        FakeGithubProjectsMapper::invoke
    )

    @Test
    fun `getProjectsForOrganisation SHOULD return mapped result WHEN api is successful`() =
        runBlockingTest {
            fakeGithubProjectApiService.result = Response.success(githubProjectsDTO)

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asSuccess().data).isEqualTo(githubProjects)
        }

    @Test
    fun `getProjectsForOrganisation SHOULD return NoProjectFound WHEN api is successful but body is null`() =
        runBlockingTest {
            fakeGithubProjectApiService.result = Response.success(null)

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asError().reason).isEqualTo(GetProjectsError.NoProjectFound)
        }

    @Test
    fun `getProjectsForOrganisation SHOULD return NoProjectFound WHEN api is not successful because of 404`() =
        runBlockingTest {
            fakeGithubProjectApiService.result = Response.error(404, ResponseBody.create(null, ""))

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asError().reason).isEqualTo(GetProjectsError.NoProjectFound)
        }

    @Test
    fun `getProjectsForOrganisation SHOULD return GenericError WHEN api is not successful because of generic error`() =
        runBlockingTest {
            fakeGithubProjectApiService.result = Response.error(500, ResponseBody.create(null, ""))

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asError().reason).isEqualTo(GetProjectsError.GenericError)
        }

    private class FakeGithubProjectApiService : GithubProjectApiService {
        lateinit var result: Response<List<JsonGithubProject>>

        override suspend fun getProjectsForOrganisation(organisation: String): Response<List<JsonGithubProject>> {
            return stubOrThrow(
                isValidInvocation = organisation == ORGANISATION,
                result = result
            )
        }
    }

    private object FakeGithubProjectsMapper {
        fun invoke(jsonProjects: List<JsonGithubProject>): List<GithubProject> {
            return stubOrThrow(
                isValidInvocation = jsonProjects == githubProjectsDTO,
                result = githubProjects
            )
        }
    }
}