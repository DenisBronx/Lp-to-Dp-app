package com.denisbrandi.githubprojects.factory

import androidx.appcompat.app.AppCompatActivity
import com.denisbrandi.githubprojects.data.mapper.*
import com.denisbrandi.githubprojects.data.remote.GithubProjectApiService
import com.denisbrandi.githubprojects.data.repository.RealGithubProjectRepository
import com.denisbrandi.githubprojects.domain.service.OrganisationValidator
import com.denisbrandi.githubprojects.domain.usecase.*
import com.denisbrandi.githubprojects.presentation.viewmodel.*
import com.denisbrandi.prelude.ListMapper
import retrofit2.Retrofit

internal class GithubProjectDIProvider(
    private val retrofit: Retrofit
) {

    private val githubProjectRepository by lazy {
        RealGithubProjectRepository(
            retrofit.create(GithubProjectApiService::class.java),
            ListMapper(GithubProjectMapper::map)::map,
            GithubProjectDetailsMapper::map
        )
    }

    private val getProjectsForOrganisation by lazy {
        GetProjectsForOrganisation(githubProjectRepository::getProjectsForOrganisation)
    }

    private val getProjectDetails by lazy {
        GetProjectDetails(githubProjectRepository::getProjectDetails)
    }

    internal fun provideGithubProjectsListViewModel(activity: AppCompatActivity): GithubProjectsListViewModel {
        return activity.getViewModel {
            GithubProjectsListViewModel(
                getProjectsForOrganisation,
                OrganisationValidator::isValidOrganisation
            )
        }
    }

    internal fun provideGithubProjectDetailsViewModel(activity: AppCompatActivity): GithubProjectDetailsViewModel {
        return activity.getViewModel {
            GithubProjectDetailsViewModel(getProjectDetails)
        }
    }

}