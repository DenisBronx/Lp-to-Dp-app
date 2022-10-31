@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

package com.denisbrandi.githubprojects.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import com.bumptech.glide.integration.compose.*
import com.denisbrandi.designsystem.*
import com.denisbrandi.githubprojects.R
import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.githubprojects.domain.model.GetProjectsError.*
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel.State.*

@Composable
internal inline fun GithubProjectsListView(
    githubProjectsListViewModel: GithubProjectsListViewModel,
    crossinline onProjectClicked: (githubProject: GithubProject) -> Unit,
    crossinline onRetry: () -> Unit
) {
    Scaffold(
        topBar = { TopBar() },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                Content(githubProjectsListViewModel, onProjectClicked, onRetry)
            }
        }
    )
}

@Composable
private fun TopBar() {
    var text by rememberSaveable { mutableStateOf("") }
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = colorResource(R.color.purple_700)),
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = defaultMargin),
                content = {
                    Text(
                        modifier = Modifier.padding(vertical = defaultMargin),
                        text = stringResource(R.string.app_name),
                        color = Color.White,
                    )
                    TextField(
                        value = text,
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                        onValueChange = { text = it }
                    )
                }
            )
        },
    )
}

@Composable
internal inline fun Content(
    githubProjectsListViewModel: GithubProjectsListViewModel,
    crossinline onProjectClicked: (githubProject: GithubProject) -> Unit,
    crossinline onRetry: () -> Unit
) {
    val uiState by githubProjectsListViewModel.state.collectAsState()
    ContentBuilder(githubProjectsListViewModel, uiState, onProjectClicked, onRetry)
}

@Composable
private inline fun ContentBuilder(
    githubProjectsListViewModel: GithubProjectsListViewModel,
    state: GithubProjectsListViewModel.State,
    crossinline onProjectClicked: (githubProject: GithubProject) -> Unit,
    crossinline onRetry: () -> Unit
) {
    when (state) {
        is Idle -> FullScreenMessage(stringResource(id = R.string.full_hint))
        is InvalidInput -> FullScreenMessage(stringResource(id = R.string.invalid_organisation_input))
        is Loading -> FullScreenLoading()
        is Error -> ErrorState(getProjectsError = state.getProjectsError, onRetry = onRetry)
        is Content -> ContentState(state.githubProjects, onProjectClicked)
    }
}

@Composable
private inline fun ErrorState(getProjectsError: GetProjectsError, crossinline onRetry: () -> Unit) {
    when (getProjectsError) {
        is NoProjectFound -> {
            ErrorView(errorMessage = stringResource(R.string.no_projects_error_message))
        }
        is GenericError -> {
            RetryErrorView(
                errorMessage = stringResource(R.string.generic_projects_error_message),
                onRetry = onRetry
            )
        }
    }
}

@Composable
private fun FullScreenMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text(
                modifier = Modifier.padding(defaultMargin),
                style = MaterialTheme.typography.bodyMedium,
                text = message
            )
        }
    )
}

@Composable
private inline fun ContentState(
    githubProjects: List<GithubProject>,
    crossinline onProjectClicked: (githubProject: GithubProject) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(vertical = halfMargin)) {
        items(githubProjects) { githubProject ->
            ProjectRow(githubProject, onProjectClicked)
        }
    }
}

@Composable
private inline fun ProjectRow(
    githubProject: GithubProject,
    crossinline onProjectClicked: (githubProject: GithubProject) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = halfMargin, horizontal = defaultMargin)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.cardview_default_elevation)),
        onClick = { onProjectClicked(githubProject) }
    ) {
        Row(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.card_height))
                .padding(horizontal = defaultMargin),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                modifier = Modifier.size(dimensionResource(id = R.dimen.thumbnail_size)),
                model = githubProject.imageUrl,
                contentDescription = stringResource(id = R.string.repository_image)
            )
            Column(
                modifier = Modifier.padding(start = defaultMargin)
            ) {
                Text(
                    text = githubProject.fullName,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1
                )
                Text(
                    text = githubProject.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
        }
    }
}