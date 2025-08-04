package com.lealapps.teste.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.lealapps.teste.model.TrainingModel
import com.lealapps.teste.repository.TrainingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ViewModel para gerenciar o estado e a comunicação com o Firebase
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                trainings = repository.getAll()

            } catch (e: FirebaseFirestoreException) {
                // Lidar com erros de acesso ao Firestore
                message = e.message ?: ""
            } finally {
                isLoading = false
            }
        }
    }

    // Funcao para enviar dados para o Firestore
    fun uploadTraining() {
        val training = TrainingModel(
            name = nameTraining,
            comment = trainingObservations,
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                repository.uploadTraining(training)
            } catch (e: Exception) {
                message = e.message
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                repository.updateTraining(documentPath, training)
                nameTraining = ""
                trainingObservations = ""
                trainingId = null
            } catch (e: Exception) {
                message = e.message
            } finally {
                isLoading = false
            }
        }

    }

    fun deleteTraining() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                repository.deleteTraining(trainingId)
                nameTraining = ""
                trainingObservations = ""
                trainingId = null
                trainings.filterNot { it.id == trainingId }
            } catch (e: Exception) {
                message = e.message
            } finally {
                isLoading = false
            }
        }

    }


}