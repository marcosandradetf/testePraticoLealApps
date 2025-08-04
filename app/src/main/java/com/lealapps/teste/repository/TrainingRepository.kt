package com.lealapps.teste.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.lealapps.teste.firebase.FirebaseService.auth
import com.lealapps.teste.firebase.FirebaseService.db
import com.lealapps.teste.model.TrainingModel
import kotlinx.coroutines.tasks.await

class TrainingRepository {
    private val collectionTraining =
        db.collection("users").document(auth.currentUser?.uid ?: "").collection("training")

    suspend fun getAll(): MutableList<TrainingModel> {
        return try {
            val result = collectionTraining.get().await()
            if (result.documents.isNotEmpty()) {
                result.documents.mapNotNull { document ->
                    document.toObject(TrainingModel::class.java)
                }.toMutableList() // Transformando para MutableList
            } else {
                mutableListOf() // Retorna uma lista mutável vazia
            }
        } catch (e: FirebaseFirestoreException) {
            // Lidar com erros de acesso ao Firestore
            Log.e(ContentValues.TAG, "Erro ao acessar a coleção training", e)
            throw e
        }
    }


    suspend fun uploadTraining(training: TrainingModel) {
        try {
            collectionTraining.document(training.id).set(training).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateTraining(documentPath: String, training: TrainingModel) {
        val updates = hashMapOf<String, Any>(
            "name" to training.name,
            "comment" to training.comment
        )

        try {
            collectionTraining.document(documentPath).update(updates).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteTraining(documentPath: String?) {

        try {
            collectionTraining.document(documentPath.toString()).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }

}