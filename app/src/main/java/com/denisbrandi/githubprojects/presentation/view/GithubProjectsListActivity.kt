package com.denisbrandi.githubprojects.presentation.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GithubProjectsListActivity : AppCompatActivity() {

    @Inject
    internal lateinit var githubProjectsListViewModel: GithubProjectsListViewModel

    @Inject
    internal lateinit var projectsListRouter: ProjectsListRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubProjectsListView(
                githubProjectsListViewModel,
                onProjectClicked = { organization, repository ->
                    projectsListRouter.openProjectDetail(organization, repository)
                }
            )
        }
    }

    interface ProjectsListRouter {
        fun openProjectDetail(organisation: String, projectName: String)
    }
}