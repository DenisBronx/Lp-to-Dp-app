package com.denisbrandi.designsystem

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.res.colorResource
import com.denisbrandi.githubprojects.R

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = colorResource(R.color.teal_200))
    }
}