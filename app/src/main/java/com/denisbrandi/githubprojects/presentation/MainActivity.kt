package com.denisbrandi.githubprojects.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.denisbrandi.githubprojects.R
import com.denisbrandi.githubprojects.domain.repository.GithubProjectRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var githubProjectRepository: GithubProjectRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            val result = githubProjectRepository.getProjectsForOrganisation("square")
            Log.d("result", result.toString())
        }
    }
}