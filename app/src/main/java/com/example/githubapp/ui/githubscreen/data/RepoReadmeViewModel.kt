package com.example.githubapp.ui.githubscreen.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.usecase.GitApiController
import com.example.githubapp.data.usecase.IGitApiController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepoReadmeViewModel(
    private val gitApiController: IGitApiController = GitApiController()
) : ViewModel() {
    
    companion object {
        private const val TAG = "RepoReadmeViewModel"
    }
    
    private val _viewState = MutableStateFlow(RepoReadmeViewState())
    val viewState: StateFlow<RepoReadmeViewState> = _viewState

    fun processIntent(intent: RepoReadmeIntent) {
        when (intent) {
            is RepoReadmeIntent.LoadReadme -> {
                loadReadme(intent.owner, intent.repo)
            }
        }
    }

    private fun loadReadme(owner: String, repo: String) {
        viewModelScope.launch {
            try {
                _viewState.value = _viewState.value.copy(isLoading = true, error = "")
                val readmeContent = gitApiController.getRepositoryReadme(owner, repo)
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    readmeContent = readmeContent
                )
            } catch (e: Exception) {
                Log.e(TAG, "loadReadme: error: ", e)
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}