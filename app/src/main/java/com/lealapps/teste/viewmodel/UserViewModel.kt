package com.lealapps.teste.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.lealapps.teste.firebase.FirebaseService.auth
import com.lealapps.teste.firebase.FirebaseService.translateFirebaseError
import com.lealapps.teste.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    var user by mutableStateOf<FirebaseUser?>(null)

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        user = firebaseAuth.currentUser
        isAuthenticated = user != null
    }

    init {
        auth.addAuthStateListener(authListener)
    }

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isAuthenticated by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var passwordUpdated by mutableStateOf(false)
    var message by mutableStateOf("")

    fun signIn() {
        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    userRepository.signIn(email, password)
                }
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun createAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            // Validação antes de tentar criar a conta
            if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                withContext(Dispatchers.Main) {
                    message = "Preencha todos os campos"
                }
                return@launch
            }

            if (password != confirmPassword) {
                withContext(Dispatchers.Main) {
                    message = "As senhas informadas não coincidem"
                }
                return@launch
            }

            try {
                withContext(Dispatchers.Main) { isLoading = true }

                userRepository.createAccount(email, password)

                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }

                auth.currentUser?.updateProfile(profileUpdates)?.await()

                withContext(Dispatchers.Main) {
                    message = "Conta criada com sucesso"
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    message = translateFirebaseError(e)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }

    fun changePassword(oldPass: String, newPass: String, confirmPass: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    userRepository.changePassword(oldPass,newPass, confirmPass)
                }
                passwordUpdated = true
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    userRepository.signOut()
                }
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }




}