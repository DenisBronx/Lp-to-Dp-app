package com.denisbrandi.githubprojects.data.model

import com.squareup.moshi.Json

internal data class JsonOwner(
    @field:Json(name = "avatar_url") val avatarUrl: String? = null
)