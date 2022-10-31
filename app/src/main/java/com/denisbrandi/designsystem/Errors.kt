package com.denisbrandi.designsystem

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import com.denisbrandi.githubprojects.R

@Composable
inline fun RetryErrorView(
    errorMessage: String = stringResource(id = R.string.generic_projects_error_message),
    crossinline onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text(
                modifier = Modifier.padding(defaultMargin),
                style = MaterialTheme.typography.bodyMedium,
                text = errorMessage
            )
            Button(
                onClick = { onRetry() },
                content = { Text(text = stringResource(R.string.retry)) })
        }
    )
}

@Composable
fun ErrorView(
    errorMessage: String = stringResource(id = R.string.generic_projects_error_message)
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text(
                modifier = Modifier.padding(defaultMargin),
                style = MaterialTheme.typography.bodyMedium,
                text = errorMessage
            )
        }
    )
}