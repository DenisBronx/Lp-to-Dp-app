package com.denisbrandi.githubprojects.data.model

import com.squareup.moshi.Json

data class JsonGithubProjectDetails(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "full_name") val fullName: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "owner") val owner: JsonOwner? = null
)