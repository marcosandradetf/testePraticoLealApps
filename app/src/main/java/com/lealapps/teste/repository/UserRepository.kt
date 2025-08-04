package com.lealapps.teste.repository

import android.widget.Toast
import com.lealapps.teste.firebase.FirebaseService.auth
import kotlinx.coroutines.tasks.await

class UserRepository {
    suspend fun createAccount(
        email: String,
        password: String,
    ): Boolean {
        // Crie a conta do usuário com o email e a senha fornecidos
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e:Exception) {
            throw e
        }
    }



    suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            throw e
        }
    }
}