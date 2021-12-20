package com.denisbrandi.githubprojects.data.repository

import com.denisbrandi.githubprojects.data.model.*
import com.denisbrandi.githubprojects.data.remote.GithubProjectApiService
import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.testutil.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.*
import retrofit2.Response
import java.io.IOException

class RealGithubProjectRepositoryTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeGithubProjectApiService = FakeGithubProjectApiService()
    private val sut = RealGithubProjectRepository(
        fakeGithubProjectApiService,
        FakeMapperFacade::invoke,
        FakeMapperFacade::invoke,
        Dispatchers.Main
    )

    @Test
    fun `EXPECT mapped githubProjects WHEN api is successful`() =
        runBlockingTest {
            fakeGithubProjectApiService.getProjectsForOrganisationResult =
                { Response.success(githubProjectsDTO) }

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asSuccess().data).isEqualTo(githubProjects)
        }

    @Test
    fun `EXPECT NoProjectFound WHEN api is successful but body is null`() =
        runBlockingTest {
            fakeGithubProjectApiService.getProjectsForOrganisationResult =
                { Response.success(null) }

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asError().reason).isEqualTo(GetProjectsError.NoProjectFound)
        }

    @Test
    fun `EXPECT NoProjectFound WHEN api is not successful because of 404`() =
        runBlockingTest {
            fakeGithubProjectApiService.getProjectsForOrganisationResult =
                { Response.error(404, ResponseBody.create(null, "")) }

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asError().reason).isEqualTo(GetProjectsError.NoProjectFound)
        }

    @Test
    fun `EXPECT GenericError WHEN api is not successful because of generic error`() =
        runBlockingTest {
            fakeGithubProjectApiService.getProjectsForOrganisationResult =
                { Response.error(500, ResponseBody.create(null, "")) }

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asError().reason).isEqualTo(GetProjectsError.GenericError)
        }

    @Test
    fun `EXPECT GenericError WHEN api is not successful because of exception`() =
        runBlockingTest {
            fakeGithubProjectApiService.getProjectsForOrganisationResult = { throw IOException() }

            val result = sut.getProjectsForOrganisation(ORGANISATION)

            assertThat(result.asError().reason).isEqualTo(GetProjectsError.GenericError)
        }

    @Test
    fun `EXPECT mapped githubProjectDetails WHEN api is successful`() = runBlockingTest {
        fakeGithubProjectApiService.getProjectDetailsResult = {
            Response.success(githubProjectDetailsDTO)
        }

        val result = sut.getProjectDetails(ORGANISATION, REPOSITORY)

        assertThat(result.asSuccess().data).isEqualTo(githubProjectDetails)
    }

    @Test
    fun `EXPECT error WHEN api is successful but body is null`() =
        runBlockingTest {
            fakeGithubProjectApiService.getProjectDetailsResult = { Response.success(null) }

            val result = sut.getProjectDetails(ORGANISATION, REPOSITORY)

            assertThat(result.isError()).isTrue()
        }

    @Test
    fun `EXPECT error WHEN api is not successful`() =
        runBlockingTest {
            fakeGithubProjectApiService.getProjectDetailsResult = {
                Response.error(500, ResponseBody.create(null, ""))
            }

            val result = sut.getProjectDetails(ORGANISATION, REPOSITORY)

            assertThat(result.isError()).isTrue()
        }

    @Test
    fun `EXPECT error WHEN api is not successful because of exception`() =
        runBlockingTest {
            val reason = IOException()
            fakeGithubProjectApiService.getProjectDetailsResult = {
                throw reason
            }

            val result = sut.getProjectDetails(ORGANISATION, REPOSITORY)

            assertThat(result.asError().reason).isEqualTo(reason)
        }

    private companion object {
        const val ORGANISATION = "square"
        const val REPOSITORY = "plastic"

        val githubProjectsDTO = listOf(JsonGithubProject(id = "1"), JsonGithubProject(id = "2"))
        val githubProjects = listOf(GithubProject(id = "1"), GithubProject(id = "2"))
        val githubProjectDetailsDTO = JsonGithubProjectDetails(id = "1")
        val githubProjectDetails = GithubProjectDetails(id = "1")
    }

    private class FakeGithubProjectApiService : GithubProjectApiService {
        lateinit var getProjectsForOrganisationResult: () -> Response<List<JsonGithubProject>>
        lateinit var getProjectDetailsResult: () -> Response<JsonGithubProjectDetails>

        override suspend fun getProjectsForOrganisation(organisation: String): Response<List<JsonGithubProject>> {
            return stubOrThrow(
                isValidInvocation = organisation == ORGANISATION,
                result = getProjectsForOrganisationResult()
            )
        }

        override suspend fun getProjectDetails(
            owner: String,
            repository: String
        ): Response<JsonGithubProjectDetails> {
            return stubOrThrow(
                isValidInvocation = listOf(owner, repository) == listOf(ORGANISATION, REPOSITORY),
                result = getProjectDetailsResult()
            )
        }
    }

    private object FakeMapperFacade {
        fun invoke(jsonProjects: List<JsonGithubProject>): List<GithubProject> {
            return stubOrThrow(
                isValidInvocation = jsonProjects === githubProjectsDTO,
                result = githubProjects
            )
        }

        fun invoke(jsonGithubProjectDetails: JsonGithubProjectDetails): GithubProjectDetails {
            return stubOrThrow(
                isValidInvocation = jsonGithubProjectDetails === githubProjectDetailsDTO,
                result = githubProjectDetails
            )
        }
    }
}