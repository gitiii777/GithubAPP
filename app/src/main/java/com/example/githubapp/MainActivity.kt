package com.example.githubapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.githubapp.data.repository.AuthCacheManager
import com.example.githubapp.data.repository.AuthManager
import com.example.githubapp.data.repository.GithubApiClient
import com.example.githubapp.ui.githubscreen.IssueDetailScreen
import com.example.githubapp.ui.githubscreen.IssuesScreen
import com.example.githubapp.ui.githubscreen.LoginScreen
import com.example.githubapp.ui.githubscreen.PopularRepoScreen
import com.example.githubapp.ui.githubscreen.RepoReadmeScreen
import com.example.githubapp.ui.githubscreen.SearchScreen
import com.example.githubapp.ui.githubscreen.UserProfileScreen
import com.example.githubapp.ui.githubscreen.UserRepoScreen
import com.example.githubapp.ui.githubscreen.data.IssueDetailViewModel
import com.example.githubapp.ui.githubscreen.data.IssuesViewModel
import com.example.githubapp.ui.githubscreen.data.LoginViewModel
import com.example.githubapp.ui.githubscreen.data.LoginViewModelFactory
import com.example.githubapp.ui.githubscreen.data.PopularRepoViewModel
import com.example.githubapp.ui.githubscreen.data.RepoReadmeViewModel
import com.example.githubapp.ui.githubscreen.data.UserProfileViewModel
import com.example.githubapp.ui.githubscreen.data.UserRepoViewModel
import com.example.githubapp.ui.theme.GithubAPPTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化GithubApiClient
        GithubApiClient.initialize(this)
        // 初始化AuthManager
        authManager = AuthManager.getInstance(
            AuthCacheManager.getInstance(this)
        )

        setContent {
            GithubAPPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(authManager = authManager)
                }
            }
        }
    }
}

@Composable
fun MainScreen(authManager: AuthManager) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            authManager = authManager,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authManager: AuthManager,
    modifier: Modifier = Modifier
) {
    Log.d(TAG, "AppNavigation: enter")

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(GithubAppRouteName.Search.title) {
            SearchScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(GithubAppRouteName.PopularRepo.title) {
            val popularRepoViewModel: PopularRepoViewModel = viewModel()
            PopularRepoScreen(
                viewModel = popularRepoViewModel,
                onRepositoryClick = { owner, repo ->
                    navController.navigate("${GithubAppRouteName.RepoReadme.title}/$owner/$repo")
                },
                onSearch = { navController.navigate(GithubAppRouteName.Search.title) }
            )
        }
        composable("${GithubAppRouteName.RepoReadme.title}/{owner}/{repo}") { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            val repoReadmeViewModel: RepoReadmeViewModel = viewModel()
            RepoReadmeScreen(
                owner = owner,
                repo = repo,
                viewModel = repoReadmeViewModel,
                onBackClick = { navController.popBackStack() },
                onListIssues = { navController.navigate("${GithubAppRouteName.Issues.title}/$owner/$repo") }
            )
        }
        composable(BottomNavItem.Profile.route) {
            val userProfileViewModel: UserProfileViewModel = viewModel()
            UserProfileScreen(
                viewModel = userProfileViewModel,
                authManager = authManager,
                onLoginClick = {
                    navController.navigate(GithubAppRouteName.Login.title)
                },
                onViewRepos = {
                    navController.navigate(GithubAppRouteName.UserRepos.title)
                },
            )
        }
        composable(GithubAppRouteName.Login.title) {
            val loginViewModel: LoginViewModel =
                viewModel(factory = LoginViewModelFactory(authManager))
            LoginScreen(
                authManager = authManager,
                viewModel = loginViewModel,
                onLoginSuccess = { navController.popBackStack() }
            )
        }
        composable(GithubAppRouteName.UserRepos.title) {
            val userRepoViewModel: UserRepoViewModel = viewModel()
            UserRepoScreen(
                viewModel = userRepoViewModel,
                onBackClick = { navController.popBackStack() },
                onRepositoryClick = { owner, repo ->
                    navController.navigate("${GithubAppRouteName.RepoReadme.title}/$owner/$repo")
                }
            )
        }
        composable("${GithubAppRouteName.Issues.title}/{owner}/{repo}") { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            val issuesViewModel: IssuesViewModel = viewModel()
            IssuesScreen(
                owner = owner,
                repo = repo,
                viewModel = issuesViewModel,
                onBackClick = { navController.popBackStack() },
                onIssueDetail = { detailOwner, detailRepo, issueNumber ->
                    navController.navigate("${GithubAppRouteName.IssueDetail.title}/$detailOwner/$detailRepo/$issueNumber")
                }
            )
        }
        composable("${GithubAppRouteName.IssueDetail.title}/{owner}/{repo}/{issueNumber}") { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            val issueNumber = backStackEntry.arguments?.getString("issueNumber")?.toIntOrNull() ?: 0
            val issueDetailViewModel: IssueDetailViewModel = viewModel()
            IssueDetailScreen(
                owner = owner,
                repo = repo,
                issueNumber = issueNumber,
                viewModel = issueDetailViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Home : BottomNavItem(GithubAppRouteName.PopularRepo.title, "首页", Icons.Default.Home)
    object Profile :
        BottomNavItem(GithubAppRouteName.Profile.title, "我的", Icons.Default.AccountCircle)
}

enum class GithubAppRouteName(val title: String) {
    PopularRepo("PopularRepo"),
    RepoReadme("RepoReadme"),
    Search("search"),
    Profile("profile"),
    Login("login"),
    UserRepos("user_repos"),
    Issues("issues"),
    IssueDetail("issue_detail"),
}