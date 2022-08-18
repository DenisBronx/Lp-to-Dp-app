package com.denisbrandi.githubprojects.data.api.fakes

import com.denisbrandi.githubprojects.data.model.*
import com.denisbrandi.githubprojects.data.remote.GithubProjectApiService
import com.denisbrandi.testutil.stubOrThrow
import retrofit2.Response

internal class FakeGithubProjectApiService(
    private val expectedOrganisation: String,
    private val expectedRepository: String
) : GithubProjectApiService {
    lateinit var getProjectsForOrganisationResult: () -> Response<List<JsonGithubProject>>
    lateinit var getProjectDetailsResult: () -> Response<JsonGithubProjectDetails>

    override suspend fun getProjectsForOrganisation(organisation: String): Response<List<JsonGithubProject>> {
        return stubOrThrow(
            isValidInvocation = expectedOrganisation == organisation,
            result = getProjectsForOrganisationResult()
        )
    }

    override suspend fun getProjectDetails(
        owner: String,
        repository: String
    ): Response<JsonGithubProjectDetails> {
        return stubOrThrow(
            isValidInvocation = listOf(
                expectedOrganisation,
                expectedRepository
            ) == listOf(
                owner,
                repository
            ),
            result = getProjectDetailsResult()
        )
    }
}