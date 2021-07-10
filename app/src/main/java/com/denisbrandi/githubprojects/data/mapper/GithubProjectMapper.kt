package com.denisbrandi.githubprojects.data.mapper

import com.denisbrandi.githubprojects.data.model.JsonGithubProject
import com.denisbrandi.githubprojects.domain.model.GithubProject

object GithubProjectMapper {

    fun map(jsonGithubProject: JsonGithubProject): GithubProject {
        return GithubProject(
            id = jsonGithubProject.id,
            name = jsonGithubProject.name.orEmpty(),
            description = jsonGithubProject.description.orEmpty()
        )
    }

}