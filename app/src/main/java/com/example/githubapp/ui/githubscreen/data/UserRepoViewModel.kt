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

/**
 * ViewModel for the user repositories screen implementing MVI architecture
 */
class UserRepoViewModel(
    private val gitApiController: IGitApiController = GitApiController()
) : ViewModel() {

    // ViewState represents the state of the UI
    private val _viewState = MutableStateFlow<UserRepoViewState>(UserRepoViewState.Loading)
    val viewState: StateFlow<UserRepoViewState> = _viewState

    init {
        processIntent(UserRepoViewIntent.Load)
    }

    /**
     * Accept intents from UI
     */
    fun processIntent(intent: UserRepoViewIntent) {
        when (intent) {
            is UserRepoViewIntent.Load -> {
                loadUserRepositories()
            }
            is UserRepoViewIntent.Refresh -> {
                refreshUserRepositories()
            }
        }
    }

    /**
     * Load user repositories
     */
    private fun loadUserRepositories() {
        viewModelScope.launch {
            try {
                _viewState.value = UserRepoViewState.Loading
                withContext(Dispatchers.IO) {
                    val repositories = gitApiController.getUserRepositories()
                    _viewState.value = UserRepoViewState.Success(repositories)
                }
            } catch (e: Exception) {
                _viewState.value = UserRepoViewState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Refresh user repositories
     */
    private fun refreshUserRepositories() {
        loadUserRepositories()
    }
}