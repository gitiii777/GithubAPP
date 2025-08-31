package com.example.githubapp.ui.githubscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.githubapp.data.repository.Issue
import com.example.githubapp.ui.githubscreen.data.IssueDetailViewIntent
import com.example.githubapp.ui.githubscreen.data.IssueDetailViewModel
import com.example.githubapp.ui.githubscreen.data.IssueDetailViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailScreen(
    owner: String,
    repo: String,
    issueNumber: Int,
    viewModel: IssueDetailViewModel,
    onBackClick: () -> Unit,
) {
    val viewState by viewModel.viewState.collectAsState()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.processIntent(IssueDetailViewIntent.LoadIssue(owner, repo, issueNumber))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text("Issue Details")
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
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
                is IssueDetailViewState -> {
                    if (state.isLoading) {
                        Text(
                            text = "Loading issue...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else if (state.error.isNotEmpty()) {
                        Text(
                            text = "Error loading issue: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else if (state.issue != null) {
                        IssueDetailContent(issue = state.issue)
                    } else {
                        Text(
                            text = "Issue not found",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IssueDetailContent(issue: Issue) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Issue 标题和编号
        Text(
            text = "#${issue.number} ${issue.title}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        // Issue 状态
        Text(
            text = "状态: ${issue.state}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp),
            color = if (issue.state == "open") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
        
        // Issue 作者
        Text(
            text = "作者: ${issue.user.login}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        // Issue 创建时间
        Text(
            text = "创建时间: ${issue.created_at}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        // Issue 更新时间
        issue.updated_at?.let { updatedAt ->
            Text(
                text = "更新时间: $updatedAt",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        // Issue 内容
        issue.body?.let { body ->
            Text(
                text = "内容:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        // Issue 标签
        issue.labels?.let { labels ->
            if (labels.isNotEmpty()) {
                Text(
                    text = "标签:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
                
                labels.forEach { label ->
                    Text(
                        text = label.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        // Issue 分配者
        issue.assignees?.let { assignees ->
            if (assignees.isNotEmpty()) {
                Text(
                    text = "分配给:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
                
                assignees.forEach { assignee ->
                    Text(
                        text = assignee.login,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}