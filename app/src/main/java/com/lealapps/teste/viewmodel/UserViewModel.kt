package com.lealapps.teste.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lealapps.teste.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isAuthenticated by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf("")

    fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                val success = userRepository.signIn(email, password)
                if (success) isAuthenticated = true
            } catch (e: Exception) {
                message = e.message ?: "Falha ao tentar fazer login"
            } finally {
                isLoading = false
            }
        }
    }

    fun createAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            if (password != confirmPassword) {
                message = "As senhas informada não coincidem"
                return@launch
            }

            try {
                isLoading = true
                val success = userRepository.createAccount(email, password)
                if (success) isAuthenticated = true
            } catch (e: Exception) {
                message = e.message ?: "Falha na criação da conta"
            } finally {
                isLoading = false
            }
        }
    }

}