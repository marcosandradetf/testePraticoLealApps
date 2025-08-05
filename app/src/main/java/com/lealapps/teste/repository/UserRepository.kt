package com.lealapps.teste.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.lealapps.teste.firebase.FirebaseService.auth
import kotlinx.coroutines.tasks.await

class UserRepository {
    suspend fun createAccount(
        email: String,
        password: String,
    ) {
        // Crie a conta do usuário com o email e a senha fornecidos
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun signIn(email: String, password: String) {
         try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun changePassword(oldPass: String, newPass: String, confirmPass: String) {
        if (newPass != confirmPass) {
            throw Exception("A nova senha e a confirmação não coincidem")
        }

        val user = auth.currentUser ?: throw Exception("Usuário não autenticado")

        try {
            val credential = EmailAuthProvider.getCredential(user.email ?: "", oldPass)
            user.reauthenticate(credential).await()

            user.updatePassword(newPass).await()

        } catch (e: Exception) {
            throw e
        }
    }

}