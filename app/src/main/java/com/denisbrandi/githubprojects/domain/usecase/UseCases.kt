package com.denisbrandi.githubprojects.domain.usecase

import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.prelude.Answer

internal typealias GetProjectsForOrganisation = suspend (organisation: String) -> Answer<List<GithubProject>, GetProjectsError>
internal typealias GetProjectDetails = suspend (owner: String, projectName: String) -> Answer<GithubProjectDetails, Throwable>