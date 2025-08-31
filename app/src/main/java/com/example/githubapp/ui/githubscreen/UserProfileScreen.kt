package com.example.githubapp.ui.githubscreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubapp.data.repository.AuthManager
import com.example.githubapp.data.repository.User
import com.example.githubapp.ui.githubscreen.data.UserProfileViewIntent
import com.example.githubapp.ui.githubscreen.data.UserProfileViewModel
import com.example.githubapp.ui.githubscreen.data.UserProfileViewState

private const val TAG = "UserProfileScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = viewModel(),
    authManager: AuthManager,
    onLoginClick: () -> Unit,
    onViewRepos: () -> Unit,
) {
    val viewState by viewModel.viewState.collectAsState()
    val isLogin by remember { authManager.isLoggedIn }
    LaunchedEffect(isLogin) {
        Log.d(TAG, "UserProfileScreen: LaunchedEffect, isLogin:$isLogin")
        if (isLogin) {
            viewModel.processIntent(UserProfileViewIntent.Load)
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
                    Text("我的")
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
            if (authManager.isLoggedIn.value) {
                // 已登录状态
                when (val state = viewState) {
                    is UserProfileViewState.Loading -> {
                        Text(
                            text = "Loading user profile...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    is UserProfileViewState.Success -> {
                        UserProfileContent(
                            user = state.user,
                            onViewRepos = onViewRepos,
                            onLogout = {
                                authManager.clearAuth()
                            }
                        )
                    }
                    is UserProfileViewState.Error -> {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                // 未登录状态
                NotLoggedInContent(
                    onLoginClick = onLoginClick
                )
            }
        }
    }
}

@Composable
fun NotLoggedInContent(
    onLoginClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "未登录",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "请登录以查看您的GitHub信息",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("登录")
            }
        }
    }
}

@Composable
fun UserProfileContent(
    user: User,
    onViewRepos: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User login name
            Text(
                text = user.login,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )

            // User display name
            user.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // User email
            user.email?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // User stats
            UserStats(
                repos = user.public_repos,
                followers = user.followers,
                following = user.following,
                modifier = Modifier.padding(top = 16.dp)
            )

            // View repositories button
            Button(
                onClick = onViewRepos,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("查看我的仓库")
            }

            // Logout button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("退出登录")
            }
        }
    }
}

@Composable
fun UserStats(
    repos: Int,
    followers: Int,
    following: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Repositories: $repos",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Followers: $followers",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Following: $following",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}