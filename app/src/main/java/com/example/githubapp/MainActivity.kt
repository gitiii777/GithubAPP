package com.example.githubapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.githubapp.ui.blank.BlankScreen
import com.example.githubapp.ui.blank.data.BlankViewModel
import com.example.githubapp.ui.githubscreen.PopularRepoScreen
import com.example.githubapp.ui.githubscreen.RepoReadmeScreen
import com.example.githubapp.ui.githubscreen.data.PopularRepoViewModel
import com.example.githubapp.ui.githubscreen.data.RepoReadmeViewModel

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubApp()
        }
    }
}

@Composable
fun GithubApp(
    navController: NavHostController = rememberNavController()
) {
    Log.d(TAG, "GithubApp: enter")
    NavHost(
        navController = navController,
        startDestination = GithubAppRouteName.Blank.title,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = GithubAppRouteName.Blank.title) {
            val blankViewModel: BlankViewModel = viewModel()
            BlankScreen(
                viewModel = blankViewModel,
                onGetRepositories = { navController.navigate(GithubAppRouteName.PopularRepo.title) }
            )
        }
        composable(route = GithubAppRouteName.PopularRepo.title) {
            val popularRepoViewModel: PopularRepoViewModel = viewModel()
            PopularRepoScreen(
                viewModel = popularRepoViewModel,
                onRepositoryClick = { owner, repo ->
                    navController.navigate("${GithubAppRouteName.RepoReadme.title}/$owner/$repo")
                },
                navController = navController
            )
        }
        composable(route = "${GithubAppRouteName.RepoReadme.title}/{owner}/{repo}") { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            val repoReadmeViewModel: RepoReadmeViewModel = viewModel()
            RepoReadmeScreen(
                owner = owner,
                repo = repo,
                viewModel = repoReadmeViewModel,
                navController = navController
            )
        }
    }
}
@Composable
fun GithubAppTheme(content: @Composable () -> Unit) {
    androidx.compose.material3.MaterialTheme(content = content)
}

enum class GithubAppRouteName(val title: String) {
    Blank("Blank"),
    PopularRepo("PopularRepo"),
    RepoReadme("RepoReadme")
}