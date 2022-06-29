package com.denisbrandi.githubprojects.data.mapper

import com.denisbrandi.githubprojects.data.model.JsonGithubProject
import com.denisbrandi.githubprojects.domain.model.GithubProject

internal object GithubProjectMapper {

    fun map(jsonGithubProject: JsonGithubProject): GithubProject {
        return GithubProject(
            id = jsonGithubProject.id,
            name = jsonGithubProject.name.orEmpty(),
            fullName = jsonGithubProject.fullName.orEmpty(),
            description = jsonGithubProject.description.orEmpty(),
            imageUrl = jsonGithubProject.owner?.avatarUrl.orEmpty()
        )
    }

}