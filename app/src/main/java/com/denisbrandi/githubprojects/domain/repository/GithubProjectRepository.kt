package com.denisbrandi.githubprojects.domain.repository

import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.prelude.Answer

interface GithubProjectRepository {

    suspend fun getProjectsForOrganisation(organisation: String): Answer<List<GithubProject>, GetProjectsError>

}