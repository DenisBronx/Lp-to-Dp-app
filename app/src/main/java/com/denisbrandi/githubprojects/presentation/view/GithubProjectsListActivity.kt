package com.denisbrandi.githubprojects.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
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
    internal lateinit var githubProjectsListViewModel: GithubProjectsListViewModel

    @Inject
    internal lateinit var projectsListRouter: ProjectsListRouter

    private lateinit var binding: ActivityProjectsListBinding
    private val adapter: GithubProjectsAdapter = GithubProjectsAdapter { project ->
        projectsListRouter.openProjectDetail(
            binding.organisationInput.text.toString(),
            project.name
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        observeViewModelState()

        binding.loadDataButton.setOnClickListener { load() }
        binding.retryButton.setOnClickListener { load() }

        setUpList()
    }

    private fun load() {
        hideKeyboard()
        githubProjectsListViewModel.loadProjects(binding.organisationInput.text.toString())
    }

    private fun setUpList() {
        binding.projectsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.projectsRecyclerView.adapter = adapter
    }

    private fun observeViewModelState() {
        githubProjectsListViewModel.state.flowWhenStarted(this) { state ->
            setDefaultViewsState()

            when (state) {
                is Idle -> binding.infoMessage.isVisible = true
                is Loading -> binding.spinner.isVisible = true
                is Content -> drawContentState(state.githubProjects)
                is Error -> drawErrorMessage(state.getProjectsError)
                is InvalidInput -> {
                    binding.errorMessage.isVisible = true
                    binding.errorMessage.text = getString(R.string.invalid_organisation_input)
                }
            }
        }
    }

    private fun setDefaultViewsState() {
        binding.infoMessage.isVisible = false
        binding.spinner.isVisible = false
        binding.errorMessage.isVisible = false
        binding.projectsRecyclerView.isVisible = false
        binding.retryButton.isVisible = false
        adapter.submitList(emptyList())
    }

    private fun drawErrorMessage(getProjectsError: GetProjectsError) {
        binding.infoMessage.isVisible = true

        when (getProjectsError) {
            is GetProjectsError.NoProjectFound -> {
                binding.infoMessage.text = getString(R.string.no_projects_error_message)
            }
            is GetProjectsError.GenericError -> {
                binding.retryButton.isVisible = true
                binding.infoMessage.text = getString(R.string.generic_projects_error_message)
            }
        }
    }

    private fun drawContentState(githubProjects: List<GithubProject>) {
        binding.projectsRecyclerView.isVisible = true
        adapter.submitList(githubProjects)
    }

    interface ProjectsListRouter {
        fun openProjectDetail(organisation: String, projectName: String)
    }
}