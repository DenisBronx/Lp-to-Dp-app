package com.denisbrandi.githubprojects.domain.repository

import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.prelude.Answer

internal interface GithubProjectRepository {

    suspend fun getProjectsForOrganisation(organisation: String): Answer<List<GithubProject>, GetProjectsError>

    suspend fun getProjectDetails(
        owner: String,
        repositoryName: String
    ): Answer<GithubProjectDetails, Throwable>

}