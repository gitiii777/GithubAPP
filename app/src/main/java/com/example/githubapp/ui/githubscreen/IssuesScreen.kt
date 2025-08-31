package com.example.githubapp.ui.githubscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.githubapp.data.repository.Issue
import com.example.githubapp.ui.githubscreen.data.IssuesViewIntent
import com.example.githubapp.ui.githubscreen.data.IssuesViewModel
import com.example.githubapp.ui.githubscreen.data.IssuesViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssuesScreen(
    owner: String,
    repo: String,
    viewModel: IssuesViewModel,
    onBackClick: () -> Unit,
    onIssueDetail: (String, String, Int) -> Unit = { _, _, _ -> },
) {
    val viewState by viewModel.viewState.collectAsState()

    // 加载数据
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.processIntent(IssuesViewIntent.LoadIssues(owner, repo))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text("Issues")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
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
                is IssuesViewState -> {
                    if (state.isLoading) {
                        Text(
                            text = "Loading issues...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else if (state.error.isNotEmpty()) {
                        Text(
                            text = "Error loading issues: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else if (state.issues.isEmpty()) {
                        Text(
                            text = "No issues found",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.issues) { issue ->
                                IssueItem(
                                    issue = issue,
                                    onClick = {
                                        onIssueDetail(owner, repo, issue.number)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IssueItem(
    issue: Issue,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "#${issue.number} ${issue.title}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "状态: ${issue.state}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp),
                color = if (issue.state == "open") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )

            issue.user.login.let { author ->
                Text(
                    text = "作者: $author",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            issue.body?.let { body ->
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "创建时间: ${issue.created_at}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}