package com.denisbrandi.githubprojects.presentation.view

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GithubProjectDetailActivity : AppCompatActivity() {

    companion object {

        private const val ORGANISATION_EXTRA = "organisation"
        private const val PROJECT_EXTRA = "project"

        fun getStartIntent(context: Context, organisation: String, projectId: String): Intent {
            return Intent(context, GithubProjectDetailActivity::class.java).apply {
                putExtra(ORGANISATION_EXTRA, organisation)
                putExtra(PROJECT_EXTRA, projectId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}