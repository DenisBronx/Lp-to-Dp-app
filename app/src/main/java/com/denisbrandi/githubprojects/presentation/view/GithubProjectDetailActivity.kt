package com.denisbrandi.githubprojects.presentation.view

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.view.isVisible
import com.denisbrandi.githubprojects.R
import com.denisbrandi.githubprojects.databinding.ActivityProjectDetailsBinding
import com.denisbrandi.githubprojects.domain.model.GithubProjectDetails
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectDetailsViewModel
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectDetailsViewModel.State.*
import com.denisbrandi.imageloading.ImageLoader
import com.denisbrandi.prelude.flowWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    @Inject
    lateinit var githubProjectDetailsViewModel: GithubProjectDetailsViewModel
    private lateinit var binding: ActivityProjectDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val organisation = intent.getStringExtra(ORGANISATION_EXTRA)!!
        val repository = intent.getStringExtra(PROJECT_EXTRA)!!

        setUpToolbar(repository)

        observeViewModel()

        binding.retryButton.setOnClickListener {
            load(organisation, repository)
        }
        load(organisation, repository)
    }

    private fun load(organisation: String, repository: String) {
        githubProjectDetailsViewModel.loadDetails(
            organisation,
            repository
        )
    }

    private fun setUpToolbar(repository: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = repository
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeViewModel() {
        githubProjectDetailsViewModel.state.flowWhenStarted(this) { state ->
            Log.d("new state", state.toString())
            binding.content.isVisible = false
            binding.spinner.isVisible = false
            binding.errorView.isVisible = false
            when (state) {
                is Loading -> binding.spinner.isVisible = true
                is Content -> drawContentState(state.githubProjectDetails)
                is Error -> binding.errorView.isVisible = true
                else -> {
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun drawContentState(githubProjectDetails: GithubProjectDetails) {
        with(binding) {
            content.isVisible = true
            labelId.text = getString(R.string.id).htmlText(githubProjectDetails.id)
            labelName.text = getString(R.string.name).htmlText(githubProjectDetails.name)
            labelFullName.text =
                getString(R.string.full_name).htmlText(githubProjectDetails.fullName)
            labelDescription.text =
                getString(R.string.description).htmlText(githubProjectDetails.description)
            labelUrl.text = getString(R.string.url).htmlLink(githubProjectDetails.url)
            labelUrl.movementMethod = LinkMovementMethod.getInstance()
            labelStargazers.text =
                getString(R.string.stargazers).htmlText(githubProjectDetails.stargazers.toString())
            labelWatchers.text =
                getString(R.string.watchers).htmlText(githubProjectDetails.watchers.toString())
            ImageLoader.loadImage(image, githubProjectDetails.imageUrl)
        }
    }

    private fun String.htmlText(value: String): Spanned {
        return HtmlCompat.fromHtml("$this<br/><b>$value</b>", FROM_HTML_MODE_COMPACT)
    }

    private fun String.htmlLink(value: String): Spanned {
        return HtmlCompat.fromHtml(
            "$this<br/><b><a href=\"$value\">$value</a></b>",
            FROM_HTML_MODE_COMPACT
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}