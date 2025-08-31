package com.example.githubapp.ui.githubscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.githubapp.ui.githubscreen.data.RepoReadmeIntent
import com.example.githubapp.ui.githubscreen.data.RepoReadmeViewModel
import com.example.githubapp.ui.githubscreen.data.RepoReadmeViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoReadmeScreen(
    owner: String,
    repo: String,
    viewModel: RepoReadmeViewModel,
    navController: NavController
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.processIntent(RepoReadmeIntent.LoadReadme(owner, repo))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text("$repo README")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = viewState) {
                is RepoReadmeViewState -> {
                    if (state.isLoading) {
                        Text(
                            text = "Loading README...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else if (state.error.isNotEmpty()) {
                        Text(
                            text = "Error loading README: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Text(
                            text = state.readmeContent,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}