package com.example.githubapp.ui.githubscreen.data

import android.util.Log
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
 * ViewModel for the user profile screen implementing MVI architecture
 */
class UserProfileViewModel(
    private val gitApiController: IGitApiController = GitApiController()
) : ViewModel() {
    companion object {
        private const val TAG = "UserProfileViewModel"
    }

    // ViewState represents the state of the UI
    private val _viewState = MutableStateFlow<UserProfileViewState>(UserProfileViewState.Loading)
    val viewState: StateFlow<UserProfileViewState> = _viewState

    init {
        Log.d(TAG, "init: ")
    }

    /**
     * Accept intents from UI
     */
    fun processIntent(intent: UserProfileViewIntent) {
        when (intent) {
            is UserProfileViewIntent.Load -> {
                loadUserProfile()
            }
            is UserProfileViewIntent.Refresh -> {
                refreshUserProfile()
            }
        }
    }

    /**
     * Load user profile
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            Log.d(TAG, "loadUserProfile: ")
            try {
                _viewState.value = UserProfileViewState.Loading
                withContext(Dispatchers.IO) {
                    val user = gitApiController.getAuthenticatedUser()
                    _viewState.value = UserProfileViewState.Success(user)
                }
            } catch (e: Exception) {
                _viewState.value = UserProfileViewState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Refresh user profile
     */
    private fun refreshUserProfile() {
        loadUserProfile()
    }
}