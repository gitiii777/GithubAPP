package com.example.githubapp.ui.githubscreen.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.repository.IssueRequest
import com.example.githubapp.data.usecase.GitApiController
import com.example.githubapp.data.usecase.IGitApiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            is RepoReadmeIntent.ShowCreateIssueDialog -> {
                _viewState.value = _viewState.value.copy(showCreateIssueDialog = true)
            }
            is RepoReadmeIntent.HideCreateIssueDialog -> {
                _viewState.value = _viewState.value.copy(showCreateIssueDialog = false)
            }
            is RepoReadmeIntent.CreateIssue -> {
                createIssue(intent.owner, intent.repo, intent.issueRequest)
            }
            is RepoReadmeIntent.ResetIssueCreationState -> {
                _viewState.value = _viewState.value.copy(
                    issueCreationSuccess = false,
                    issueCreationError = ""
                )
            }
            is RepoReadmeIntent.ShowOptionsMenu -> {
                _viewState.value = _viewState.value.copy(showOptionsMenu = true)
            }
            is RepoReadmeIntent.HideOptionsMenu -> {
                _viewState.value = _viewState.value.copy(showOptionsMenu = false)
            }
        }
    }

    private fun loadReadme(owner: String, repo: String) {
        viewModelScope.launch {
            try {
                _viewState.value = _viewState.value.copy(isLoading = true, error = "")
                withContext(Dispatchers.IO) {
                    val readmeData = gitApiController.getRepositoryReadme(owner, repo)
                    val readmeContent = if (readmeData.encoding == "base64") {
                        String(android.util.Base64.decode(readmeData.content, android.util.Base64.DEFAULT))
                    } else {
                        readmeData.content
                    }
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        readmeContent = readmeContent
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadReadme: error: ", e)
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
    
    private fun createIssue(owner: String, repo: String, issueRequest: IssueRequest) {
        viewModelScope.launch {
            try {
                _viewState.value = _viewState.value.copy(
                    issueCreationLoading = true,
                    issueCreationError = ""
                )
                withContext(Dispatchers.IO) {
                    gitApiController.createIssue(owner, repo, issueRequest)

                    _viewState.value = _viewState.value.copy(
                        issueCreationLoading = false,
                        issueCreationSuccess = true,
                        showCreateIssueDialog = false
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "createIssue: error: ", e)
                _viewState.value = _viewState.value.copy(
                    issueCreationLoading = false,
                    issueCreationError = e.message ?: "Unknown error"
                )
            }
        }
    }
}