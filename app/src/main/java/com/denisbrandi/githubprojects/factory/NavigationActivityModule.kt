package com.denisbrandi.githubprojects.factory

import android.content.Context
import com.denisbrandi.githubprojects.presentation.view.GithubProjectsListActivity
import com.denisbrandi.navigation.Navigator
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object NavigationActivityModule {

    @Provides
    fun provideNavigator(
        @ActivityContext context: Context
    ): Navigator = Navigator(context)

    @Provides
    fun provideProjectsListRouter(
        navigator: Navigator
    ): GithubProjectsListActivity.ProjectsListRouter = navigator

}