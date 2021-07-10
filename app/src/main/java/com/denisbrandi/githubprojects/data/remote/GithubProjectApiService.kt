package com.denisbrandi.githubprojects.data.remote

import com.denisbrandi.githubprojects.data.model.JsonGithubProject
import retrofit2.Response
import retrofit2.http.*

interface GithubProjectApiService {

    @GET("orgs/{organisation}/repos")
    suspend fun getProjectsForOrganisation(@Path("organisation") organisation: String): Response<List<JsonGithubProject>>

}