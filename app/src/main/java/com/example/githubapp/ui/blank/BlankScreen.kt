package com.example.githubapp.ui.blank

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.githubapp.R
import com.example.githubapp.ui.blank.data.BlankViewModel
import com.example.githubapp.ui.blank.data.BlankViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlankScreen(
    viewModel: BlankViewModel,
    onGetRepositories: () -> Unit = {},
    onSearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text("Github App")
                },
                actions = {
                    IconButton(onClick = onSearch) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                val viewState by viewModel.viewState.collectAsState()
                
                when (val state = viewState) {
                    is BlankViewState.Loading -> {
                        Text(
                            text = "Loading...",
                        )
                    }
                    is BlankViewState.Success -> {
                        Text(
                            text = "Welcome to Github App",
                        )
                    }
                    is BlankViewState.Error -> {
                        Text(
                            text = "Error: ${state.message}",
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    onClick = {
                        onGetRepositories()
                    }
                ) {
                    Text(
                        text = "获取仓库"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlankScreenPreview() {
    MaterialTheme {
        // In preview, we can't create a real ViewModel, so we just show the UI with default state
        BlankScreen(
            viewModel = TODO("Provide a preview ViewModel instance"),
            onGetRepositories = {},
            onSearch = {}
        )
    }
}