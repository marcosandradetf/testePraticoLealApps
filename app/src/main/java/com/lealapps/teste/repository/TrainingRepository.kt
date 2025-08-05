package com.lealapps.teste.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.lealapps.teste.firebase.FirebaseService.auth
import com.lealapps.teste.firebase.FirebaseService.db
import com.lealapps.teste.model.TrainingModel
import kotlinx.coroutines.tasks.await

class TrainingRepository {

    private fun getTrainingCollection() = auth.currentUser?.let { user ->
        db.collection("users").document(user.uid).collection("training")
    }

    suspend fun getAll(): MutableList<TrainingModel> {
        val collectionTraining = getTrainingCollection() ?: throw IllegalStateException("Usuário não autenticado")

        return try {
            val result = collectionTraining.get().await()
            if (result.documents.isNotEmpty()) {
                result.documents.mapNotNull { it.toObject(TrainingModel::class.java) }.toMutableList()
            } else {
                mutableListOf()
            }
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }

    suspend fun uploadTraining(training: TrainingModel) {
        val collectionTraining = getTrainingCollection() ?: throw IllegalStateException("Usuário não autenticado")

        try {
            collectionTraining.document(training.id).set(training).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateTraining(documentPath: String, training: TrainingModel) {
        val collectionTraining = getTrainingCollection() ?: throw IllegalStateException("Usuário não autenticado")

        val updates = mapOf(
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
        val collectionTraining = getTrainingCollection() ?: throw IllegalStateException("Usuário não autenticado")

        try {
            collectionTraining.document(documentPath ?: "").delete().await()
        } catch (e: Exception) {
            throw e
        }
    }
}
