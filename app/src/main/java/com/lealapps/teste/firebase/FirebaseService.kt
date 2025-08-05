package com.lealapps.teste.firebase

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code.*
import com.google.firebase.storage.StorageException


object FirebaseService {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    @SuppressLint("StaticFieldLeak")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val storageRef = Firebase.storage.reference

    fun translateFirebaseError(e: Exception): String {
        return when (e) {
            // 🔐 Firebase Auth
            is FirebaseAuthInvalidUserException ->
                "Usuário não encontrado. Verifique o e-mail."

            is FirebaseAuthInvalidCredentialsException ->
                "E-mail ou senha inválidos."

            is FirebaseAuthUserCollisionException ->
                "Esse e-mail já está em uso."

            is FirebaseAuthWeakPasswordException ->
                "A senha deve ter pelo menos 6 caracteres."

            is FirebaseAuthRecentLoginRequiredException ->
                "Você precisa fazer login novamente para realizar essa ação."

            is FirebaseAuthException ->
                "Erro de autenticação: ${e.message ?: "Ocorreu um erro."}"

            // 🔥 Firestore
            is FirebaseFirestoreException -> {
                when (e.code) {
                    PERMISSION_DENIED -> "Você não tem permissão para acessar esses dados."
                    UNAVAILABLE -> "O serviço está indisponível. Verifique sua conexão."
                    DEADLINE_EXCEEDED -> "Tempo limite excedido. Tente novamente."
                    ABORTED -> "A operação foi abortada. Tente novamente."
                    UNAUTHENTICATED -> "Você precisa estar autenticado para isso."
                    NOT_FOUND -> "Dado não encontrado."
                    CANCELLED -> "A operação foi cancelada."
                    DATA_LOSS -> "Ocorreu uma perda de dados inesperada."
                    INVALID_ARGUMENT -> "Argumento inválido enviado para o servidor."
                    INTERNAL -> "Erro interno no servidor. Tente novamente."
                    else -> "Erro no Firestore: ${e.message}"
                }
            }

            // 🗂 Firebase Storage
            is StorageException -> {
                when (e.errorCode) {
                    StorageException.ERROR_OBJECT_NOT_FOUND -> "O arquivo não foi encontrado."
                    StorageException.ERROR_BUCKET_NOT_FOUND -> "O bucket de armazenamento não existe."
                    StorageException.ERROR_PROJECT_NOT_FOUND -> "Projeto Firebase não foi encontrado."
                    StorageException.ERROR_QUOTA_EXCEEDED -> "Limite de armazenamento excedido."
                    StorageException.ERROR_NOT_AUTHENTICATED -> "Você precisa estar autenticado para isso."
                    StorageException.ERROR_NOT_AUTHORIZED -> "Você não tem permissão para essa ação."
                    StorageException.ERROR_RETRY_LIMIT_EXCEEDED -> "Muitas tentativas. Tente mais tarde."
                    StorageException.ERROR_INVALID_CHECKSUM -> "Erro de integridade do arquivo."
                    StorageException.ERROR_CANCELED -> "Operação cancelada pelo usuário."
                    else -> "Erro no armazenamento: ${e.message}"
                }
            }

            // Outros Firebase erros genéricos
            is FirebaseException ->
                "Erro Firebase: ${e.message ?: "Erro desconhecido."}"

            // Qualquer outro tipo de erro
            else ->
                e.message ?: "Ocorreu um erro desconhecido."
        }
    }

}

