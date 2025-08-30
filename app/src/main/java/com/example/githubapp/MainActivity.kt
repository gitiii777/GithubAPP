package com.example.githubapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubapp.ui.BlankScreen
import com.example.githubapp.ui.viewModel.BlankViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel: BlankViewModel = viewModel()
                    BlankScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GithubAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}