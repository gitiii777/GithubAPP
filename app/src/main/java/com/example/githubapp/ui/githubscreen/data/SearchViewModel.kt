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
 * ViewModel for the search screen implementing MVI architecture
 */
class SearchViewModel(
    private val gitApiController: IGitApiController = GitApiController()
) : ViewModel() {

    // ViewState represents the state of the UI
    private val _viewState = MutableStateFlow<SearchViewState>(SearchViewState.Initial)
    val viewState: StateFlow<SearchViewState> = _viewState

    fun processIntent(intent: SearchViewIntent) {
        when (intent) {
            is SearchViewIntent.Search -> {
                searchRepositories(intent.query)
            }
        }
    }

    /**
     * Search repositories by query
     */
    private fun searchRepositories(query: String) {
        viewModelScope.launch {
            try {
                _viewState.value = SearchViewState.Loading
                withContext(Dispatchers.IO) {
                    val repositories = gitApiController.searchRepositories(
                        query,
                        "best match",
                        "desc"
                    ).sortedBy {
                        -it.stargazers_count
                    }
                    _viewState.value = SearchViewState.Success(repositories)
                }
            } catch (e: Exception) {
                _viewState.value = SearchViewState.Error(e.message ?: "Unknown error")
            }
        }
    }
}