package com.denisbrandi.githubprojects.data.mapper

import com.denisbrandi.githubprojects.data.model.JsonGithubProjectDetails
import com.denisbrandi.githubprojects.domain.model.GithubProjectDetails

object GithubProjectDetailsMapper {

    fun map(jsonGithubProjectDetails: JsonGithubProjectDetails): GithubProjectDetails {
        return GithubProjectDetails(
            id = jsonGithubProjectDetails.id,
            name = jsonGithubProjectDetails.name.orEmpty(),
            fullName = jsonGithubProjectDetails.fullName.orEmpty(),
            description = jsonGithubProjectDetails.description.orEmpty(),
            imageUrl = jsonGithubProjectDetails.owner?.avatarUrl.orEmpty(),
            url = jsonGithubProjectDetails.url.orEmpty(),
            stargazers = jsonGithubProjectDetails.stargazers ?: 0,
            watchers = jsonGithubProjectDetails.watchers ?: 0
        )
    }

}