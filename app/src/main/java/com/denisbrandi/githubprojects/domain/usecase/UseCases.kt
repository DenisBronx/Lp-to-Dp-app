package com.denisbrandi.githubprojects.domain.usecase

import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.prelude.Answer

typealias GetProjectsForOrganisation = suspend (organisation: String) -> Answer<List<GithubProject>, GetProjectsError>
typealias GetProjectDetails = suspend (owner: String, projectName: String) -> Answer<GithubProjectDetails, Throwable>