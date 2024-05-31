package com.lealapps.teste.api

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lealapps.teste.models.ExerciseModel
import com.lealapps.teste.models.TrainingModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel para gerenciar o estado e a comunicação com o Firebase
class ExerciseViewModel : ViewModel() {
    val nameTraining = mutableStateOf("")
    val commTraining = mutableStateOf("")
    val nameExercise = mutableStateOf("")
    val commExercise = mutableStateOf("")
    var selectedImageUri by mutableStateOf<Uri?>(null)
    private var uriDownload by mutableStateOf<Uri?>(null)
    private val db = Firebase.firestore
    private val collectionReference = db.collection("training")

    var trainingState by mutableStateOf<TrainingModel?>(null)
    var exerciseState by mutableStateOf<ExerciseModel?>(null)

    fun updateTrainingState(selectedTraining: TrainingModel) {
        trainingState = selectedTraining
    }

    fun setNameTraining(name: String) {
        nameTraining.value = name
    }
    fun setCommTraining(comm: String) {
        commTraining.value = comm
    }

    fun setNameExercise(name: String) {
        nameExercise.value = name
    }
    fun setCommExercise(comm: String) {
        commExercise.value = comm
    }

    fun clearFieldsTraining() {
        setNameTraining("")
        setCommTraining("")
    }

    fun clearFieldsExercise() {
        setNameExercise("")
        setCommExercise("")
        selectedImageUri = null
    }

    suspend fun getTraining(
        collectionExists: (Boolean) -> Unit,
        setData: ( MutableList<TrainingModel> ) -> Unit,
        disableLoading: (Boolean) -> Unit
    ){
        try {
            val result = db.collection("training").get().await()
            // Verifica se a coleção possui documentos
            collectionExists(!result.isEmpty)

            if (!result.isEmpty) {
                val workoutsList = mutableListOf<TrainingModel>()
                for ((_, document) in result.documents.withIndex()) {
                    val trainingModel = document.toObject<TrainingModel>()
                    // Definindo o ID do documento no objeto
                    trainingModel?.id = document.id
                    // Atribuir o índice do exercício dentro do objeto TrainingModel
                    trainingModel?.exercises?.forEachIndexed { index, exercise ->
                        exercise.id = index
                    }
                    if (trainingModel != null) {
                        workoutsList.add(trainingModel)
                    }
                }
                setData(workoutsList)
            }
        } catch (e: FirebaseFirestoreException) {
            // Lidar com erros de acesso ao Firestore
            Log.e(ContentValues.TAG, "Erro ao acessar a coleção training", e)
        } finally {
            disableLoading(false)
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

    // Função para fazer upload de mídia para o Firebase
    private fun uploadMedia() {
        if (selectedImageUri != null) {
            val storageRef = Firebase.storage.reference
            // Cria uma referência para o arquivo no Firebase Storage
            val fileRef = storageRef.child("images/${selectedImageUri?.lastPathSegment}")

            // Envia o arquivo para o Firebase Storage
            val uploadTask = selectedImageUri?.let { fileRef.putFile(it) }

            // Registra observadores para lidar com o sucesso ou falha do upload
            uploadTask?.addOnSuccessListener { taskSnapshot ->
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
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            }?.addOnFailureListener { exception ->
                // Falha no upload
                // Aqui você pode lidar com a falha no upload, exibindo uma mensagem de erro, registrando a exceção, etc.
            }
        }

    }

     fun uploadExercise(documentPath: String) {
         try {
             uploadMedia()
         } catch (e: Exception) {
             Log.d("Media", "Não foi possível enviar a imagem")
         } finally {
             val exercise = ExerciseModel(
                 name = nameExercise.value,
                 comment = commExercise.value,
                 image = uriDownload.toString(),
             )

             collectionReference.document(documentPath).update("exercises", FieldValue.arrayUnion(exercise))
         }
    }

    fun updateExercise(documentPath: String, exerciseIndex: Int) {
        try {
            if (exerciseState?.image?.toUri() != selectedImageUri){
                uploadMedia()
                Log.d("uploadMedia","Entrou")
            } else {
                uriDownload = selectedImageUri
            }

        } catch (e: Exception) {
            Log.d("Media", "Não foi possível enviar a imagem")
        } finally {

            deleteExercise(
                documentPath = documentPath,
                exerciseIndex = exerciseIndex
            )

            uploadExercise(documentPath = documentPath)

        }

    }



    fun deleteExercise(documentPath: String, exerciseIndex: Int) {
        val documentReference = collectionReference.document(documentPath)

        // Obtenha os dados do documento
        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                // Obtenha a lista de exercícios do documento
                val exercises = documentSnapshot.toObject<TrainingModel>()?.exercises

                // Verifique se a lista de exercícios não é nula e se o índice do exercício é válido
                if (exercises != null && exerciseIndex >= 0 && exerciseIndex < exercises.size) {
                    // Remova o exercício da lista com base no índice especificado
                    exercises.removeAt(exerciseIndex)

                    // Atualize a matriz exercises no documento Firestore
                    documentReference.update("exercises", exercises)
                        .addOnSuccessListener {
                            // Sucesso ao atualizar, exercício excluído com sucesso
                            Log.d("deleteExercise", "Exercício excluído com sucesso.")
                        }
                        .addOnFailureListener { exception ->
                            // Falha ao atualizar
                            Log.e("deleteExercise", "Erro ao excluir exercício.", exception)
                        }
                } else {
                    // A lista de exercícios é nula ou o índice do exercício é inválido
                    Log.e("deleteExercise", "Erro ao excluir exercício: lista de exercícios nula ou índice inválido.")
                }
            }
            .addOnFailureListener { exception ->
                // Falha ao obter os dados do documento
                Log.e("deleteExercise", "Erro ao obter documento.", exception)
            }
    }



}