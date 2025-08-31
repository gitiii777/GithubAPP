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

class PopularRepoViewModel(private val gitApiController: IGitApiController = GitApiController()) : ViewModel() {
    companion object {
        private const val TAG = "PopularRepoViewModel"
    }
    
    private val _viewState = MutableStateFlow(PopularRepoViewState())
    val viewState: StateFlow<PopularRepoViewState> = _viewState

    fun processIntent(intent: PopularRepoViewIntent) {
        Log.d(TAG, "processIntent: $intent")
        when (intent) {
            is PopularRepoViewIntent.Load -> {
                getRepositories()
            }
        }
    }

    private fun getRepositories() {
        viewModelScope.launch {
            try {
                _viewState.value = _viewState.value.copy(isLoading = true, message = "")
                withContext(Dispatchers.IO) {
                    val repositories = gitApiController.getRepositories()
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        repositories = repositories,
                        message = ""
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "getRepositories: error: ", e)
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    message = e.message ?: "Unknown error"
                )
            }
        }
    }
}