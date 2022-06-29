package com.denisbrandi.githubprojects.data.model

import com.squareup.moshi.Json

internal data class JsonGithubProjectDetails(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "full_name") val fullName: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "owner") val owner: JsonOwner? = null,
    @field:Json(name = "html_url") val url: String? = null,
    @field:Json(name = "stargazers_count") val stargazers: Int? = null,
    @field:Json(name = "watchers_count") val watchers: Int? = null
)