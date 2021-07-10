package com.denisbrandi.githubprojects.domain.usecase

import com.denisbrandi.githubprojects.domain.model.GetProjectsError
import com.denisbrandi.githubprojects.domain.model.GithubProject
import com.denisbrandi.prelude.Answer

typealias GetProjectsForOrganisation = suspend (organisation: String) -> Answer<List<GithubProject>, GetProjectsError>