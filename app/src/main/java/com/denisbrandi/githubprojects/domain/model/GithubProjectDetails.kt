package com.denisbrandi.githubprojects.domain.model

data class GithubProjectDetails(
    val id: String,
    val name: String = "",
    val fullName: String = "",
    val description: String = "",
    val imageUrl: String = ""
)