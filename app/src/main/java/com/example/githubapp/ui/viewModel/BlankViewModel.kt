package com.example.githubapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.ui.data.BlankViewIntent
import com.example.githubapp.ui.data.BlankViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the blank screen implementing MVI architecture
 */
class BlankViewModel : ViewModel() {

    // ViewState represents the state of the UI
    private val _viewState = MutableStateFlow<BlankViewState>(BlankViewState.Success)
    val viewState: StateFlow<BlankViewState> = _viewState

    // ViewIntent represents user actions
    private val _viewIntent = Channel<BlankViewIntent>(Channel.UNLIMITED)
    val viewIntent = _viewIntent

    init {
        handleIntents()
    }

    /**
     * Process user intents
     */
    private fun handleIntents() {
        viewModelScope.launch {
            _viewIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is BlankViewIntent.Load -> {
                        loadContent()
                    }
                    is BlankViewIntent.Refresh -> {
                        refreshContent()
                    }
                }
            }
        }
    }

    /**
     * Process load intent
     */
    private fun loadContent() {
        // For a blank page, we just show success state
        _viewState.value = BlankViewState.Success
    }

    /**
     * Process refresh intent
     */
    private fun refreshContent() {
        // For a blank page, we just show success state
        _viewState.value = BlankViewState.Success
    }

    /**
     * Accept intents from UI
     */
    fun processIntent(intent: BlankViewIntent) {
        viewModelScope.launch {
            _viewIntent.send(intent)
        }
    }
}