package com.denisbrandi.designsystem

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import com.denisbrandi.githubprojects.R

@Composable
fun RetryErrorView(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text(text = stringResource(id = R.string.generic_projects_error_message))
            Button(
                onClick = { onRetry() },
                content = { Text(text = stringResource(R.string.retry)) })
        }
    )
}