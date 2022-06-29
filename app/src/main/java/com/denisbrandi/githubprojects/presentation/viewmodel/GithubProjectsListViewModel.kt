package com.denisbrandi.githubprojects.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.githubprojects.domain.usecase.GetProjectsForOrganisation
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel.State
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel.State.*
import kotlinx.coroutines.launch

internal class GithubProjectsListViewModel(
    private val getProjectsForOrganisation: GetProjectsForOrganisation,
    private val isValidOrganisation: (organisation: String) -> Boolean
) : BaseViewModel<State>(Idle) {

    fun loadProjects(organisation: String) {
        if (!isValidOrganisation(organisation)) {
            setState(InvalidInput)
            return
        }

        setState(Loading)

        viewModelScope.launch {
            val result = getProjectsForOrganisation(organisation)
            result.fold(
                success = { projects ->
                    setState(Content(projects))
                },
                error = { error ->
                    setState(Error(error))
                }
            )
        }
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Content(val githubProjects: List<GithubProject>) : State()
        data class Error(val getProjectsError: GetProjectsError) : State()
        object InvalidInput : State()
    }

}