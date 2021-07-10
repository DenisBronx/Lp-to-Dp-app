package com.denisbrandi.githubprojects.factory

import com.denisbrandi.githubprojects.data.mapper.GithubProjectMapper
import com.denisbrandi.githubprojects.data.remote.GithubProjectApiService
import com.denisbrandi.githubprojects.data.repository.RealGithubProjectRepository
import com.denisbrandi.githubprojects.domain.repository.GithubProjectRepository
import com.denisbrandi.prelude.ListMapper
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object GithubProjectSingletonModule {

    @Provides
    @Singleton
    fun provideGithubProjectRepository(retrofit: Retrofit): GithubProjectRepository {
        return RealGithubProjectRepository(
            retrofit.create(GithubProjectApiService::class.java),
            ListMapper(GithubProjectMapper::map)::map
        )
    }
}