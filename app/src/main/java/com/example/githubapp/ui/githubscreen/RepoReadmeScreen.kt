package com.example.githubapp.ui.githubscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.githubapp.ui.githubscreen.data.RepoReadmeIntent
import com.example.githubapp.ui.githubscreen.data.RepoReadmeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoReadmeScreen(
    owner: String,
    repo: String,
    viewModel: RepoReadmeViewModel = viewModel(),
    navController: NavController? = null
) {
    val viewState by viewModel.viewState.collectAsState()
    
    LaunchedEffect(owner, repo) {
        viewModel.processIntent(RepoReadmeIntent.LoadReadme(owner, repo))
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("README")
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewState.isLoading -> {
                    Text(text = "Loading README...")
                }
                viewState.readmeContent.isNotEmpty() -> {
                    ReadmeContent(readmeContent = viewState.readmeContent)
                }
                viewState.error.isNotEmpty() -> {
                    Text(
                        text = "Error: ${viewState.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    Text(text = "No README content available")
                }
            }
        }
    }
}

@Composable
fun ReadmeContent(readmeContent: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = readmeContent,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace
            )
        )
    }
}