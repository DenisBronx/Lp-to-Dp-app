package com.denisbrandi.githubprojects.data.repository

import com.denisbrandi.githubprojects.data.model.JsonGithubProject
import com.denisbrandi.githubprojects.data.remote.GithubProjectApiService
import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.githubprojects.domain.repository.GithubProjectRepository
import com.denisbrandi.prelude.Answer
import kotlinx.coroutines.*

class RealGithubProjectRepository(
        private val githubProjectApiService: GithubProjectApiService,
        private val githubProjectsMapper: (jsonProjects: List<JsonGithubProject>) -> List<GithubProject>
) : GithubProjectRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun getProjectsForOrganisation(organisation: String): Answer<List<GithubProject>, GetProjectsError> {
        return with(coroutineScope) {
            val apiResult = githubProjectApiService.getProjectsForOrganisation(organisation)

            if (apiResult.isSuccessful) {
                apiResult.body()?.let { Answer.Success(githubProjectsMapper(it)) }
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
}