package com.denisbrandi.githubprojects.data.repository

import com.denisbrandi.githubprojects.data.api.fakes.FakeGithubProjectApiService
import com.denisbrandi.githubprojects.data.model.*
import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.testutil.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.*
import retrofit2.Response
import java.io.IOException

class RealGithubProjectRepositoryTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeGithubProjectApiService = FakeGithubProjectApiService(ORGANISATION, REPOSITORY)
    private val sut = RealGithubProjectRepository(
        fakeGithubProjectApiService,
        FakeMapperFacade::invoke,
        FakeMapperFacade::invoke,
        Dispatchers.Main
    )

    @Test
    fun `EXPECT mapped githubProjects WHEN api is successful`() = runTest {
        fakeGithubProjectApiService.getProjectsForOrganisationResult =
            { Response.success(githubProjectsDTO) }

        val result = sut.getProjectsForOrganisation(ORGANISATION)

        assertThat(result.asSuccess().data).isEqualTo(githubProjects)
    }

    @Test
    fun `EXPECT NoProjectFound WHEN api is successful but body is null`() = runTest {
        fakeGithubProjectApiService.getProjectsForOrganisationResult =
            { Response.success(null) }

        val result = sut.getProjectsForOrganisation(ORGANISATION)

        assertThat(result.asError().reason).isEqualTo(GetProjectsError.NoProjectFound)
    }

    @Test
    fun `EXPECT NoProjectFound WHEN api is not successful because of 404`() = runTest {
        fakeGithubProjectApiService.getProjectsForOrganisationResult =
            { Response.error(404, ResponseBody.create(null, "")) }

        val result = sut.getProjectsForOrganisation(ORGANISATION)

        assertThat(result.asError().reason).isEqualTo(GetProjectsError.NoProjectFound)
    }

    @Test
    fun `EXPECT GenericError WHEN api is not successful because of generic error`() = runTest {
        fakeGithubProjectApiService.getProjectsForOrganisationResult =
            { Response.error(500, ResponseBody.create(null, "")) }

        val result = sut.getProjectsForOrganisation(ORGANISATION)

        assertThat(result.asError().reason).isEqualTo(GetProjectsError.GenericError)
    }

    @Test
    fun `EXPECT GenericError WHEN api is not successful because of exception`() = runTest {
        fakeGithubProjectApiService.getProjectsForOrganisationResult = { throw IOException() }

        val result = sut.getProjectsForOrganisation(ORGANISATION)

        assertThat(result.asError().reason).isEqualTo(GetProjectsError.GenericError)
    }

    @Test
    fun `EXPECT mapped githubProjectDetails WHEN api is successful`() = runTest {
        fakeGithubProjectApiService.getProjectDetailsResult = {
            Response.success(githubProjectDetailsDTO)
        }

        val result = sut.getProjectDetails(ORGANISATION, REPOSITORY)

        assertThat(result.asSuccess().data).isEqualTo(githubProjectDetails)
    }

    @Test
    fun `EXPECT error WHEN api is successful but body is null`() = runTest {
        fakeGithubProjectApiService.getProjectDetailsResult = { Response.success(null) }

        val result = sut.getProjectDetails(ORGANISATION, REPOSITORY)

        assertThat(result.isError()).isTrue()
    }

    @Test
    fun `EXPECT error WHEN api is not successful`() = runTest {
        fakeGithubProjectApiService.getProjectDetailsResult = {
            Response.error(500, ResponseBody.create(null, ""))
        }

        val result = sut.getProjectDetails(ORGANISATION, REPOSITORY)

        assertThat(result.isError()).isTrue()
    }

    @Test
    fun `EXPECT error WHEN api is not successful because of exception`() = runTest {
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