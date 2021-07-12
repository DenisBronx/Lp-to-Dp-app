package com.denisbrandi.navigation

import android.content.Context
import com.denisbrandi.githubprojects.presentation.view.*

class Navigator(
    private val context: Context
) : GithubProjectsListActivity.ProjectsListRouter {
    override fun openProjectDetail(organisation: String, projectName: String) {
        context.startActivity(
            GithubProjectDetailActivity.getStartIntent(
                context,
                organisation,
                projectName
            )
        )
    }
}