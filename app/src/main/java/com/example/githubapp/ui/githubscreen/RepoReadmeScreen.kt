package com.example.githubapp.ui.githubscreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubapp.data.repository.IssueRequest
import com.example.githubapp.ui.githubscreen.data.RepoReadmeIntent
import com.example.githubapp.ui.githubscreen.data.RepoReadmeViewModel
import com.example.githubapp.ui.githubscreen.data.RepoReadmeViewState

private const val TAG = "RepoReadmeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoReadmeScreen(
    owner: String,
    repo: String,
    viewModel: RepoReadmeViewModel,
    onBackClick: () -> Unit,
    onListIssues: () -> Unit,
) {
    val viewState by viewModel.viewState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var issueTitle by remember { mutableStateOf("") }
    var issueBody by remember { mutableStateOf("") }
    val bottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        Log.d(TAG, "RepoReadmeScreen: LaunchedEffect-LoadReadme")
        viewModel.processIntent(RepoReadmeIntent.LoadReadme(owner, repo))
    }

    LaunchedEffect(viewState.issueCreationSuccess) {
        Log.d(TAG, "RepoReadmeScreen: LaunchedEffect-showSnackbar")
        if (viewState.issueCreationSuccess) {
            snackbarHostState.showSnackbar("Issue created successfully!")
            viewModel.processIntent(RepoReadmeIntent.ResetIssueCreationState)
        }
    }
    
    LaunchedEffect(viewState.issueCreationError) {
        if (viewState.issueCreationError.isNotEmpty()) {
            snackbarHostState.showSnackbar("Failed to create issue: ${viewState.issueCreationError}")
            viewModel.processIntent(RepoReadmeIntent.ResetIssueCreationState)
        }
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
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        viewModel.processIntent(RepoReadmeIntent.ShowOptionsMenu)
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "选项"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center,
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
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
        
        // 选项菜单
        if (viewState.showOptionsMenu) {
            AlertDialog(
                onDismissRequest = { 
                    viewModel.processIntent(RepoReadmeIntent.HideOptionsMenu)
                },
                title = {
                    Text("选项")
                },
                text = {
                    Column {
                        TextButton(
                            onClick = {
                                viewModel.processIntent(RepoReadmeIntent.HideOptionsMenu)
                                viewModel.processIntent(RepoReadmeIntent.ShowCreateIssueDialog)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("创建 Issue")
                        }
                        TextButton(
                            onClick = {
                                viewModel.processIntent(RepoReadmeIntent.HideOptionsMenu)
                                onListIssues()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("查看 Issues")
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { 
                            viewModel.processIntent(RepoReadmeIntent.HideOptionsMenu)
                        }
                    ) {
                        Text("取消")
                    }
                }
            )
        }
        
        // 创建 Issue 底部弹框
        if (viewState.showCreateIssueDialog) {
            ModalBottomSheet(
                onDismissRequest = { 
                    viewModel.processIntent(RepoReadmeIntent.HideCreateIssueDialog)
                },
                sheetState = bottomSheetState,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "创建 Issue",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    OutlinedTextField(
                        value = issueTitle,
                        onValueChange = { issueTitle = it },
                        label = { Text("标题") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    
                    OutlinedTextField(
                        value = issueBody,
                        onValueChange = { issueBody = it },
                        label = { Text("内容") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .height(200.dp),
                        maxLines = 10
                    )
                    
                    TextButton(
                        onClick = {
                            if (issueTitle.isNotBlank()) {
                                val issueRequest = IssueRequest(
                                    title = issueTitle,
                                    body = issueBody.ifBlank { null }
                                )
                                viewModel.processIntent(
                                    RepoReadmeIntent.CreateIssue(owner, repo, issueRequest)
                                )
                                issueTitle = ""
                                issueBody = ""
                            }
                        },
                        enabled = issueTitle.isNotBlank() && !viewState.issueCreationLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("创建")
                    }
                }
            }
        }
    }
}