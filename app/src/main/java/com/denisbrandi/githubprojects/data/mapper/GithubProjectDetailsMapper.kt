package com.denisbrandi.githubprojects.data.mapper

import com.denisbrandi.githubprojects.data.model.JsonGithubProjectDetails
import com.denisbrandi.githubprojects.domain.model.GithubProjectDetails

object GithubProjectDetailsMapper {

    fun map(jsonGithubProjectDetails: JsonGithubProjectDetails): GithubProjectDetails {
        return GithubProjectDetails(
            id = jsonGithubProjectDetails.id,
            name = jsonGithubProjectDetails.fullName.orEmpty(),
            description = jsonGithubProjectDetails.description.orEmpty()
        )
    }

}