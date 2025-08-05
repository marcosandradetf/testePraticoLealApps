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

    private fun getExerciseRef() = auth.currentUser?.let { user ->
        db.collection("users").document(user.uid).collection("exercises")
    }

    private fun getTrainingRef() = auth.currentUser?.let { user ->
        db.collection("users").document(user.uid).collection("training")
    }

    suspend fun getExercisesByTrainingId(trainingId: String): List<ExerciseModel> {
        val exerciseRef = getExerciseRef() ?: throw IllegalStateException("Usuário não autenticado")

        return try {
            val result = exerciseRef
                .whereEqualTo("trainingId", trainingId)
                .get()
                .await()

            if (result.isEmpty) emptyList()
            else result.documents.mapNotNull { it.toObject<ExerciseModel>() }
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }

    suspend fun uploadExercise(selectedImageUri: Uri, exercise: ExerciseModel): String {
        val exerciseRef = getExerciseRef() ?: throw IllegalStateException("Usuário não autenticado")
        val fileRef = storageRef.child("images/${selectedImageUri.lastPathSegment}")

        try {
            fileRef.putFile(selectedImageUri).await()
            val downloadUri = fileRef.downloadUrl.await()
            exerciseRef.document(exercise.id)
                .set(exercise.copy(image = downloadUri.toString()))
                .await()

            return downloadUri.toString()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateExercise(
        exerciseId: String,
        exercise: ExerciseModel,
        selectedImageUri: Uri
    ): String {
        val exerciseRef = getExerciseRef() ?: throw IllegalStateException("Usuário não autenticado")

        // Verifica se o URI é diferente da imagem já salva
        if (selectedImageUri.toString() == exercise.image) {
            val updates = mapOf(
                "name" to exercise.name,
                "comment" to exercise.comment
                // não atualiza a imagem
            )
            exerciseRef.document(exerciseId).update(updates).await()
            return exercise.image // mantém a imagem atual
        }

        val fileRef = storageRef.child("images/${selectedImageUri.lastPathSegment}")
        try {
            fileRef.putFile(selectedImageUri).await()
            val downloadUri = fileRef.downloadUrl.await()

            val updates = mapOf(
                "name" to exercise.name,
                "comment" to exercise.comment,
                "image" to downloadUri.toString()
            )

            exerciseRef.document(exerciseId).update(updates).await()
            return downloadUri.toString()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteExercise(exerciseId: String) {
        val exerciseRef = getExerciseRef() ?: throw IllegalStateException("Usuário não autenticado")

        try {
            exerciseRef.document(exerciseId).delete().await()

        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getTraining(documentPath: String): TrainingModel? {
        val trainingRef = getTrainingRef() ?: throw IllegalStateException("Usuário não autenticado")

        return try {
            val training = trainingRef.document(documentPath).get().await()
            training.toObject<TrainingModel>()
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }
}
