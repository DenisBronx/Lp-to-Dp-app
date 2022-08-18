package com.denisbrandi.githubprojects.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.denisbrandi.githubprojects.domain.model.GithubProjectDetails
import com.denisbrandi.githubprojects.domain.usecase.GetProjectDetails
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectDetailsViewModel.State
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectDetailsViewModel.State.*
import kotlinx.coroutines.launch

internal class GithubProjectDetailsViewModel(
    private val getProjectDetails: GetProjectDetails
) : BaseViewModel<State>(Idle) {

    fun loadDetails(owner: String, repository: String) {
        setState(Loading)

        viewModelScope.launch {
            getProjectDetails(owner, repository).fold(
                success = { details ->
                    setState(Content(details))
                },
                error = {
                    setState(Error)
                }
            )
        }
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(val githubProjectDetails: GithubProjectDetails) : State
        object Error : State
    }

}