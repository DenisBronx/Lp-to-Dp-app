package com.denisbrandi.githubprojects.domain.model

internal data class GithubProjectDetails(
    val id: String,
    val name: String = "",
    val fullName: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val url: String = "",
    val stargazers: Int = 0,
    val watchers: Int = 0
)