package com.denisbrandi.githubprojects.data.remote

import com.denisbrandi.githubprojects.data.model.*
import retrofit2.Response
import retrofit2.http.*

internal interface GithubProjectApiService {

    @GET("orgs/{organisation}/repos")
    suspend fun getProjectsForOrganisation(
        @Path("organisation") organisation: String
    ): Response<List<JsonGithubProject>>

    @GET("repos/{owner}/{repository}")
    suspend fun getProjectDetails(
        @Path("owner") owner: String,
        @Path("repository") repository: String
    ): Response<JsonGithubProjectDetails>

}