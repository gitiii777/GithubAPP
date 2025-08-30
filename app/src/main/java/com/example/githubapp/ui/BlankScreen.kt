package com.example.githubapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubapp.ui.data.BlankViewState
import com.example.githubapp.ui.viewModel.BlankViewModel

/**
 * Blank screen UI using Jetpack Compose and MVI architecture
 */
@Composable
fun BlankScreen(viewModel: BlankViewModel) {
    val viewState by viewModel.viewState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = viewState) {
            is BlankViewState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.h6
                )
            }
            is BlankViewState.Success -> {
                Text(
                    text = "Blank Screen",
                    style = MaterialTheme.typography.h6
                )
            }
            is BlankViewState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlankScreenPreview() {
    MaterialTheme {
        BlankScreen(BlankViewModel())
    }
}