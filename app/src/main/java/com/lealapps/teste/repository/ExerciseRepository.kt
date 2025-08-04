package com.lealapps.teste.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import com.lealapps.teste.firebase.FirebaseService.auth
import com.lealapps.teste.firebase.FirebaseService.db
import com.lealapps.teste.firebase.FirebaseService.storageRef
import com.lealapps.teste.model.ExerciseModel
import com.lealapps.teste.model.TrainingModel
import kotlinx.coroutines.tasks.await

class ExerciseRepository {

    private val exerciseRef = if (auth.currentUser != null)
        db.collection("users").document(auth.currentUser?.uid ?: "").collection("exercises")
    else null

    private val trainingRef = if (auth.currentUser != null)
        db.collection("users").document(auth.currentUser?.uid ?: "").collection("training")
    else null

    suspend fun getExercisesByTrainingId(trainingId: String): List<ExerciseModel> {
        return try {
            val result = exerciseRef
                ?.whereEqualTo("trainingId", trainingId)  // Filtra os exercícios pelo trainingId
                ?.get()
                ?.await()

            if (result?.isEmpty == true) {
                emptyList()  // Retorna uma lista vazia se não encontrar exercícios
            } else {
                result?.documents?.mapNotNull { document ->
                    document.toObject<ExerciseModel>()
                } ?: emptyList()
            }
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }

    suspend fun uploadExercise(selectedImageUri: Uri, exercise: ExerciseModel) {
        val fileRef = storageRef.child("images/${selectedImageUri.lastPathSegment}")

        try {
            // Envia o arquivo para o Firebase Storage
            fileRef.putFile(selectedImageUri).await()

            // Obtém a URL de download após o upload ser concluído
            val downloadUri = fileRef.downloadUrl.await()

            exerciseRef?.document(
                exercise.id
            )?.set(exercise.copy(image = downloadUri.toString()))?.await()

        } catch (e: Exception) {
            // Tratar erros de upload
            throw e
        }
    }

    suspend fun updateExercise(
        exerciseId: String,
        exercise: ExerciseModel,
        selectedImageUri: Uri
    ): String {
        val fileRef = storageRef.child("images/${selectedImageUri.lastPathSegment}")

        try {
            // Envia o arquivo para o Firebase Storage
            fileRef.putFile(selectedImageUri).await()

            // Obtém a URL de download após o upload ser concluído
            val downloadUri = fileRef.downloadUrl.await()

            val updates = hashMapOf<String, Any>(
                "name" to exercise.name,
                "comment" to exercise.comment,
                "image" to downloadUri.toString()
            )

            exerciseRef?.document(exerciseId)?.update(
                updates
            )?.await()

            return downloadUri.toString()

        } catch (e: Exception) {
            throw e
        }

    }

    suspend fun deleteExercise(exerciseId: String) {
        try {
            exerciseRef?.document(exerciseId)?.delete()?.await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getTraining(
        documentPath: String
    ): TrainingModel? {
        return try {
            val training = trainingRef?.document(documentPath)?.get()?.await()
            training?.toObject<TrainingModel>()

        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }


}