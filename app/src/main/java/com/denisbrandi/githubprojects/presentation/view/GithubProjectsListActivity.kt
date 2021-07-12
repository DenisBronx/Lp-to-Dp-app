package com.denisbrandi.githubprojects.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.denisbrandi.githubprojects.R
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel
import com.denisbrandi.prelude.flowWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GithubProjectsListActivity : AppCompatActivity() {

    @Inject
    lateinit var githubProjectsListViewModel: GithubProjectsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        githubProjectsListViewModel.state.flowWhenStarted(this) { state ->
            Log.d("newState", state.toString())
        }
    }
}