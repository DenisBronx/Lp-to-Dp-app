package com.denisbrandi.githubprojects.domain.usecase

import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.prelude.Answer

internal fun interface GetProjectsForOrganisation {
    suspend operator fun invoke(organisation: String): Answer<List<GithubProject>, GetProjectsError>
}

internal fun interface GetProjectDetails {
    suspend operator fun invoke(
        owner: String,
        projectName: String
    ): Answer<GithubProjectDetails, Throwable>
}