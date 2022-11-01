@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

package com.denisbrandi.githubprojects.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
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
    crossinline onProjectClicked: (organization: String, repository: String) -> Unit
) {
    var organizationText by rememberSaveable { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar(
                organizationText = organizationText,
                onOrganizationChange = { organizationText = it },
                onLoadClick = { githubProjectsListViewModel.loadProjects(organizationText) }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                Content(
                    githubProjectsListViewModel = githubProjectsListViewModel,
                    onProjectClicked = { githubProject ->
                        onProjectClicked(organizationText, githubProject.name)
                    },
                    onRetry = { githubProjectsListViewModel.loadProjects(organizationText) }
                )
            }
        }
    )
}

@Composable
private fun TopBar(
    organizationText: String,
    onOrganizationChange: (String) -> Unit,
    onLoadClick: () -> Unit
) {
    Column(
        modifier = Modifier.background(colorResource(R.color.purple_500))
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = colorResource(R.color.purple_500)),
            title = {
                Text(
                    text = stringResource(R.string.app_name),
                    color = Color.White,
                )
            },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextField(
                modifier = Modifier.padding(bottom = defaultMargin),
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = {
                    Text(
                        modifier = Modifier.padding(noMargin),
                        text = stringResource(id = R.string.enter_organisation_name),
                        color = Color.White
                    )
                },
                value = organizationText,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    cursorColor = colorResource(R.color.teal_200),
                    focusedIndicatorColor = colorResource(R.color.teal_200),
                    unfocusedIndicatorColor = colorResource(R.color.teal_200),
                    textColor = Color.White
                ),
                onValueChange = onOrganizationChange
            )
            FloatingActionButton(
                modifier = Modifier.padding(horizontal = defaultMargin),
                shape = FloatingActionButtonDefaults.largeShape,
                containerColor = colorResource(R.color.teal_200),
                content = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.search),
                        tint = Color.White
                    )
                },
                onClick = onLoadClick
            )
        }
    }
}

@Composable
internal inline fun Content(
    githubProjectsListViewModel: GithubProjectsListViewModel,
    crossinline onProjectClicked: (githubProject: GithubProject) -> Unit,
    crossinline onRetry: () -> Unit
) {
    val uiState by githubProjectsListViewModel.state.collectAsState()
    ContentBuilder(uiState, onProjectClicked, onRetry)
}

@Composable
private inline fun ContentBuilder(
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