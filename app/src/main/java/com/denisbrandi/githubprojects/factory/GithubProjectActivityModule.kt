package com.denisbrandi.githubprojects.factory

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.denisbrandi.githubprojects.domain.repository.GithubProjectRepository
import com.denisbrandi.githubprojects.presentation.viewmodel.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object GithubProjectActivityModule {

    @Provides
    fun provideGithubProjectsListViewModel(
        @ActivityContext context: Context,
        githubProjectRepository: GithubProjectRepository
    ): GithubProjectsListViewModel {
        return (context as AppCompatActivity).getViewModel {
            GithubProjectsListViewModel(githubProjectRepository::getProjectsForOrganisation)
        }
    }

}