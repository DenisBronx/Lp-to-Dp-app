package com.denisbrandi.githubprojects.data.repository

import com.denisbrandi.githubprojects.data.model.*
import com.denisbrandi.githubprojects.data.remote.GithubProjectApiService
import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.githubprojects.domain.repository.GithubProjectRepository
import com.denisbrandi.prelude.Answer
import kotlinx.coroutines.*

class RealGithubProjectRepository(
    private val githubProjectApiService: GithubProjectApiService,
    private val mapProjects: (jsonProjects: List<JsonGithubProject>) -> List<GithubProject>,
    private val mapProjectDetails: (jsonProjectDetails: JsonGithubProjectDetails) -> GithubProjectDetails
) : GithubProjectRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun getProjectsForOrganisation(organisation: String): Answer<List<GithubProject>, GetProjectsError> {
        return with(coroutineScope) {
            val apiResult = githubProjectApiService.getProjectsForOrganisation(organisation)

            if (apiResult.isSuccessful) {
                apiResult.body()?.let { Answer.Success(mapProjects(it)) }
                    ?: Answer.Error(GetProjectsError.NoProjectFound)
            } else {
                if (apiResult.code() == 404) {
                    Answer.Error(GetProjectsError.NoProjectFound)
                } else {
                    Answer.Error(GetProjectsError.GenericError)
                }
            }
        }
    }

    override suspend fun getProjectDetails(
        owner: String,
        repositoryName: String
    ): Answer<GithubProjectDetails, Throwable> {
        return with(coroutineScope) {
            val apiResult = githubProjectApiService.getProjectDetails(owner, repositoryName)
            if (apiResult.isSuccessful) {
                apiResult.body()?.let { Answer.Success(mapProjectDetails(it)) }
                    ?: Answer.Error(Throwable("Null body"))
            } else {
                Answer.Error(Throwable("Response error"))
            }
        }
    }
}