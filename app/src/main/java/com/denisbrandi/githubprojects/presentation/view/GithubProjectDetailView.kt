@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

package com.denisbrandi.githubprojects.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import com.bumptech.glide.integration.compose.*
import com.denisbrandi.designsystem.*
import com.denisbrandi.githubprojects.R
import com.denisbrandi.githubprojects.domain.model.GithubProjectDetails
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectDetailsViewModel
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectDetailsViewModel.State
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectDetailsViewModel.State.*

@Composable
internal inline fun GithubProjectDetailView(
    githubProjectDetailsViewModel: GithubProjectDetailsViewModel,
    organisation: String,
    repository: String,
    crossinline onBack: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(repository, onBack) },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                Content(githubProjectDetailsViewModel, organisation, repository)
            }
        }
    )
}

@Composable
private inline fun TopBar(repository: String, crossinline onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = repository) },
        navigationIcon = {
            IconButton(content = {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }, onClick = { onBack() })
        }
    )
}

@Composable
private fun Content(
    githubProjectDetailsViewModel: GithubProjectDetailsViewModel,
    organisation: String,
    repository: String
) {
    val uiState by githubProjectDetailsViewModel.state.collectAsState()
    ContentBuilder(githubProjectDetailsViewModel, organisation, repository, uiState)
}

@Composable
private fun ContentBuilder(
    githubProjectDetailsViewModel: GithubProjectDetailsViewModel,
    organisation: String,
    repository: String,
    state: State
) {
    when (state) {
        is Idle -> {
            githubProjectDetailsViewModel.loadDetails(organisation, repository)
            FullScreenLoading()
        }
        is Loading -> FullScreenLoading()
        is Content -> ContentDetails(state.githubProjectDetails)
        is Error -> RetryErrorView {
            githubProjectDetailsViewModel.loadDetails(organisation, repository)
        }
    }
}

@Composable
private fun ContentDetails(githubProjectDetails: GithubProjectDetails) {
    Column(
        modifier = Modifier
            .padding(horizontal = defaultMargin, vertical = halfMargin)
            .fillMaxWidth(),
        content = {
            ContentImage(githubProjectDetails.imageUrl)
            ContentText(stringResource(R.string.id), githubProjectDetails.id)
            ContentText(stringResource(R.string.name), githubProjectDetails.name)
            ContentText(stringResource(R.string.full_name), githubProjectDetails.fullName)
            ContentText(stringResource(R.string.description), githubProjectDetails.description)
            ContentLink(stringResource(R.string.url), githubProjectDetails.url)
            ContentText(
                stringResource(R.string.stargazers),
                githubProjectDetails.stargazers.toString()
            )
            ContentText(stringResource(R.string.watchers), githubProjectDetails.watchers.toString())
        }
    )
}

@Composable
private fun ColumnScope.ContentImage(imageUrl: String) {
    GlideImage(
        modifier = Modifier
            .width(dimensionResource(R.dimen.image_size))
            .height(dimensionResource(R.dimen.image_size))
            .align(Alignment.CenterHorizontally),
        model = imageUrl,
        contentDescription = stringResource(R.string.repository_image)
    )
}

@Composable
private fun ContentText(labelName: String, labelValue: String) {
    val text = getContentString(labelName, labelValue)
    val spanStyle = getContentSpanStyle(labelName, text)
    Text(text = AnnotatedString(text, listOf(spanStyle)))
}

private fun getContentString(labelName: String, labelValue: String) = "$labelName\n$labelValue"

private fun getContentSpanStyle(
    labelName: String,
    text: String,
    color: Color = Color.Unspecified
) = AnnotatedString.Range(
    SpanStyle(fontWeight = FontWeight.Bold, color = color),
    start = labelName.length,
    end = text.length
)

@Composable
private fun ContentLink(labelName: String, labelValue: String) {
    val text = getContentString(labelName, labelValue)
    val spanStyle = getContentSpanStyle(labelName, text, Color.Blue)
    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = AnnotatedString(text, listOf(spanStyle)),
        onClick = {
            if (it > labelName.length) {
                uriHandler.openUri(labelValue)
            }
        }
    )
}