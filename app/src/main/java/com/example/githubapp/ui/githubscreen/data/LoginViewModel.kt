package com.example.githubapp.ui.githubscreen.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.repository.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the login screen implementing MVI architecture
 */
class LoginViewModel(
    private val authManager: AuthManager
) : ViewModel() {

    // ViewState represents the state of the UI
    private val _viewState = MutableStateFlow<LoginViewState>(LoginViewState.Initial)
    val viewState: StateFlow<LoginViewState> = _viewState

    /**
     * Accept intents from UI
     */
    fun processIntent(intent: LoginViewIntent) {
        when (intent) {
            is LoginViewIntent.Login -> {
                login(intent.username, intent.password)
            }
        }
    }

    /**
     * Login with username and password
     */
    private fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _viewState.value = LoginViewState.Loading
                withContext(Dispatchers.IO) {
                    // GitHub API使用Personal Access Token而不是用户名/密码
                    if (authManager.isAccountCorrect(username, password)) {
                        _viewState.value = LoginViewState.Success
                    } else {
                        _viewState.value = LoginViewState.Error("用户名或密码不正确")
                    }
                }
            } catch (e: Exception) {
                _viewState.value = LoginViewState.Error("登录失败: ${e.message}")
            }
        }
    }
}