package com.denisbrandi.githubprojects.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.denisbrandi.githubprojects.R
import com.denisbrandi.githubprojects.databinding.ActivityProjectsListBinding
import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel.State.*
import com.denisbrandi.prelude.flowWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GithubProjectsListActivity : AppCompatActivity() {

    @Inject
    lateinit var githubProjectsListViewModel: GithubProjectsListViewModel

    private lateinit var binding: ActivityProjectsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        githubProjectsListViewModel.state.flowWhenStarted(this) { state -> drawState(state) }

        binding.loadDataButton.setOnClickListener {
            githubProjectsListViewModel.loadProjects(binding.organisationInput.text.toString())
        }
    }

    private fun drawState(state: GithubProjectsListViewModel.State) {
        Log.d("newState", state.toString())
        binding.spinner.isVisible = false
        binding.errorMessage.isVisible = false
        binding.projectsRecyclerView.isVisible = false
        binding.retryButton.isVisible = false

        when (state) {
            is Loading -> binding.spinner.isVisible = true
            is Content -> drawContentState(state.githubProjects)
            is Error -> drawErrorMessage(state.getProjectsError)
            is InvalidInput -> {
                binding.errorMessage.isVisible = true
                binding.errorMessage.text = getString(R.string.invalid_organisation_input)
            }
            else -> {
            }
        }
    }

    private fun drawErrorMessage(getProjectsError: GetProjectsError) {
        binding.errorMessage.isVisible = true

        when (getProjectsError) {
            is GetProjectsError.NoProjectFound -> {
                binding.errorMessage.text = getString(R.string.no_projects_error_message)
            }
            is GetProjectsError.GenericError -> {
                binding.errorMessage.text = getString(R.string.generic_projects_error_message)
                binding.retryButton.isVisible = true
            }
        }
    }

    private fun drawContentState(githubProjects: List<GithubProject>) {

    }
}