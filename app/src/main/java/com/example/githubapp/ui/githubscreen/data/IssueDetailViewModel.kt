package com.example.githubapp.ui.githubscreen.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.usecase.GitApiController
import com.example.githubapp.data.usecase.IGitApiController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IssueDetailViewModel(
    private val gitApiController: IGitApiController = GitApiController()
) : ViewModel() {
    
    private val _viewState = MutableStateFlow(IssueDetailViewState())
    val viewState: StateFlow<IssueDetailViewState> = _viewState

    fun processIntent(intent: IssueDetailViewIntent) {
        when (intent) {
            is IssueDetailViewIntent.LoadIssue -> {
                loadIssue(intent.owner, intent.repo, intent.issueNumber)
            }
        }
    }

    private fun loadIssue(owner: String, repo: String, issueNumber: Int) {
        viewModelScope.launch {
            try {
                _viewState.value = _viewState.value.copy(isLoading = true, error = "")
                // 由于API限制，我们通过获取所有issues然后筛选特定issue的方式来实现
                val issues = gitApiController.getRepositoryIssues(owner, repo, null)
                val issue = issues.find { it.number == issueNumber }
                
                if (issue != null) {
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        issue = issue
                    )
                } else {
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        error = "Issue not found"
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