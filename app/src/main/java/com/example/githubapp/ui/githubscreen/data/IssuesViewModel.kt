package com.example.githubapp.ui.githubscreen.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.usecase.GitApiController
import com.example.githubapp.data.usecase.IGitApiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IssuesViewModel(
    private val gitApiController: IGitApiController = GitApiController()
) : ViewModel() {
    
    private val _viewState = MutableStateFlow(IssuesViewState())
    val viewState: StateFlow<IssuesViewState> = _viewState

    fun processIntent(intent: IssuesViewIntent) {
        when (intent) {
            is IssuesViewIntent.LoadIssues -> {
                loadIssues(intent.owner, intent.repo, intent.state)
            }
            is IssuesViewIntent.Refresh -> {
                // 刷新逻辑可以根据需要实现
            }
        }
    }

    private fun loadIssues(owner: String, repo: String, state: String?) {
        viewModelScope.launch {
            try {
                _viewState.value = _viewState.value.copy(isLoading = true, error = "")
                withContext(Dispatchers.IO) {
                    val issues = gitApiController.getRepositoryIssues(owner, repo, state)
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        issues = issues
                    )
                }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}