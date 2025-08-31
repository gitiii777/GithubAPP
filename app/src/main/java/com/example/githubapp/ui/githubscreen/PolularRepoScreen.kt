package com.example.githubapp.ui.githubscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.githubapp.data.repository.Repository
import com.example.githubapp.ui.githubscreen.data.PopularRepoViewIntent
import com.example.githubapp.ui.githubscreen.data.PopularRepoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularRepoScreen(
    viewModel: PopularRepoViewModel,
    onRepositoryClick: (String, String) -> Unit = { _, _ -> },
) {
    val viewState by viewModel.viewState.collectAsState()
    
    // 当屏幕加载时触发一次数据加载
    LaunchedEffect(Unit) {
        viewModel.processIntent(PopularRepoViewIntent.Load)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text("热门仓库")
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewState.isLoading -> {
                    Text(text = "Loading...")
                }
                viewState.repositories.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(viewState.repositories) { repository ->
                            RepositoryItem(
                                repository = repository,
                                onClick = { onRepositoryClick(repository.owner.login, repository.name) }
                            )
                        }
                    }
                }
                viewState.message.isNotEmpty() -> {
                    Text(
                        text = "Error: ${viewState.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    Text(text = "No repositories found")
                }
            }
        }
    }
}

@Composable
fun RepositoryItem(
    repository: Repository,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = repository.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = repository.description ?: "No description",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Stars: ${repository.stargazers_count} | Language: ${repository.language ?: "Unknown"}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}