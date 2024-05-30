package com.lealapps.teste.api

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lealapps.teste.models.ExerciseModel
import com.lealapps.teste.models.TrainingModel

// ViewModel para gerenciar o estado e a comunicação com o Firebase
class ExerciseViewModel : ViewModel() {
    val nameTraining = mutableStateOf("")
    val commTraining = mutableStateOf("")
    val nameExercise = mutableStateOf("")
    val commExercise = mutableStateOf("")
    private var uriDownload by mutableStateOf<Uri?>(null)
    private val db = Firebase.firestore
    private val collectionReference = db.collection("training")

    var trainingState by mutableStateOf<TrainingModel?>(null)
    fun updateTrainingState(selectedTraining: TrainingModel) {
        trainingState = selectedTraining
    }

    fun setNameTraining(name: String) {
        nameTraining.value = name
    }
    fun setCommTraining(comm: String) {
        commTraining.value = comm
    }

    private fun setNameExercise(name: String) {
        nameExercise.value = name
    }
    private fun setCommExercise(comm: String) {
        commExercise.value = comm
    }

    fun clearFieldsTraining() {
        setNameTraining("")
        setCommTraining("")
    }

    fun clearFieldsExercise() {
        setNameExercise("")
        setCommExercise("")
    }


    // Função para fazer upload de mídia para o Firebase
    fun uploadMedia(uri: Uri) {
        val storageRef = Firebase.storage.reference
        // Cria uma referência para o arquivo no Firebase Storage
        val fileRef = storageRef.child("images/${uri.lastPathSegment}")

        // Envia o arquivo para o Firebase Storage
        val uploadTask = fileRef.putFile(uri)

        // Registra observadores para lidar com o sucesso ou falha do upload
        uploadTask.addOnSuccessListener { taskSnapshot ->
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    uriDownload = downloadUri
                    uploadTraining()
                } else {
                    // Handle failures
                    // ...
                }
            }
        }.addOnFailureListener { exception ->
            // Falha no upload
            // Aqui você pode lidar com a falha no upload, exibindo uma mensagem de erro, registrando a exceção, etc.
        }
    }

    // Funcao para enviar dados para o Firestore
    fun uploadTraining() {
        val training = TrainingModel(
            name = nameTraining.value,
            comment = commTraining.value,
        )
        collectionReference.document().set(training)

    }

    fun updateTraining(documentPath: String) {
        val updates = hashMapOf<String, Any>(
            "name" to nameTraining.value,
            "comment" to commTraining.value
        )

        collectionReference.document(documentPath).update(updates)
        setNameTraining("")
        setCommTraining("")
    }


    fun deleteTraining(documentPath: String) {
        collectionReference.document(documentPath).delete()
    }

    fun uploadExercise(documentPath: String) {
        val exercise = ExerciseModel(
            name = nameExercise.value,
            comment = commExercise.value,
            image = uriDownload.toString(),
        )

        collectionReference.document(documentPath).update("exercises", FieldValue.arrayUnion(exercise))
    }

    fun updateExercise(documentPath: String) {
        val updates = hashMapOf<String, Any>(
            "name" to nameExercise.value,
            "comment" to commExercise.value,
            "image" to uriDownload.toString()
        )

        collectionReference.document(documentPath).update(updates)
        setNameTraining("")
        setCommTraining("")
    }

    fun deleteExercise(documentPath: String) {
        val updates = hashMapOf<String, Any>(
            "exercises.$documentPath" to FieldValue.delete()
        )

        collectionReference.document(documentPath).update(updates)
    }


}