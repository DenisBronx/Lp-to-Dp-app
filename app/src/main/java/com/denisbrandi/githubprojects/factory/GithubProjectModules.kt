package com.denisbrandi.githubprojects.factory

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.denisbrandi.githubprojects.presentation.viewmodel.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object GithubProjectSingletonModule {

    @Provides
    @Singleton
    fun provideGithubProjectProvider(retrofit: Retrofit): GithubProjectDIProvider {
        return GithubProjectDIProvider(retrofit)
    }
}

@Module
@InstallIn(ActivityComponent::class)
internal object GithubProjectActivityModule {

    @Provides
    fun provideGithubProjectsListViewModel(
        @ActivityContext context: Context,
        githubProjectDIProvider: GithubProjectDIProvider
    ): GithubProjectsListViewModel {
        return githubProjectDIProvider.provideGithubProjectsListViewModel(context as AppCompatActivity)
    }

    @Provides
    fun provideGithubProjectDetailsViewModel(
        @ActivityContext context: Context,
        githubProjectDIProvider: GithubProjectDIProvider
    ): GithubProjectDetailsViewModel {
        return githubProjectDIProvider.provideGithubProjectDetailsViewModel(context as AppCompatActivity)
    }

}