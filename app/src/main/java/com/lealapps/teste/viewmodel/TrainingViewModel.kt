package com.lealapps.teste.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.lealapps.teste.firebase.FirebaseService.translateFirebaseError
import com.lealapps.teste.model.TrainingModel
import com.lealapps.teste.repository.TrainingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("MutableCollectionMutableState")
class TrainingViewModel : ViewModel() {
    private val repository = TrainingRepository()

    var nameTraining by mutableStateOf("")
    var trainingObservations by mutableStateOf("")
    var message by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var trainingId by mutableStateOf<String?>(null)
    var trainings by mutableStateOf(mutableListOf<TrainingModel>())

    fun clearFieldsTraining() {
        nameTraining = ""
        trainingObservations = ""
    }

    fun getAll() {
        viewModelScope.launch {
            isLoading = true
            try {
                trainings = withContext(Dispatchers.IO) {
                    repository.getAll()
                }
            } catch (e: FirebaseFirestoreException) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun uploadTraining() {
        val training = TrainingModel(
            name = nameTraining,
            comment = trainingObservations,
        )

        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    repository.uploadTraining(training)
                }
                message = "Treino salvo com sucesso"
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun updateTraining(documentPath: String) {
        val training = TrainingModel(
            name = nameTraining,
            comment = trainingObservations,
        )

        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    repository.updateTraining(documentPath, training)
                }
                nameTraining = ""
                trainingObservations = ""
                trainingId = null
                message = "Treino atualizado com sucesso"
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteTraining() {
        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    repository.deleteTraining(trainingId)
                }
                nameTraining = ""
                trainingObservations = ""
                trainings = trainings.filterNot { it.id == trainingId }.toMutableList()
                trainingId = null
                message = "Treino excluído com sucesso"
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }



}