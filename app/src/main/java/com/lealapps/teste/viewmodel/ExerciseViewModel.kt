package com.lealapps.teste.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lealapps.teste.model.ExerciseModel
import com.lealapps.teste.model.TrainingModel
import com.lealapps.teste.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

// ViewModel para gerenciar o estado e a comunicação com o Firebase
class ExerciseViewModel : ViewModel() {
    private val repository = ExerciseRepository()

    var isLoading by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)
    var nameExercise by mutableStateOf("")

    var exerciseObservations by mutableStateOf("")
    var selectedImageUri by mutableStateOf<Uri?>(null)

    var trainingState by mutableStateOf<TrainingModel?>(null)
    var exerciseState by mutableStateOf<ExerciseModel?>(null)

    var exercises by mutableStateOf<List<ExerciseModel>>(emptyList())


    fun clearFieldsExercise() {
        nameExercise = ""
        exerciseObservations = ""
        selectedImageUri = null
    }

    fun uploadExercise(trainingId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (selectedImageUri != null) {
                try {
                    isLoading = true

                    val exercise = ExerciseModel(
                        id = UUID.randomUUID().toString(),
                        name = nameExercise,
                        comment = exerciseObservations,
                        image = "",
                        trainingId = trainingId
                    )

                    repository.uploadExercise(selectedImageUri!!, exercise)
                } catch (e: Exception) {
                    Log.e("uploadExercise", "Erro ao fazer upload do exercício", e)
                    message = e.message
                } finally {
                    isLoading = false
                }
            }
        }
    }


    fun updateExercise(exerciseId: String, trainingId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true

                val exercise = ExerciseModel(
                    id = UUID.randomUUID().toString(),
                    name = nameExercise,
                    comment = exerciseObservations,
                    image = "",
                    trainingId = trainingId
                )

                if (selectedImageUri != null)
                    repository.updateExercise(exerciseId, exercise, selectedImageUri!!)
            } catch (e: Exception) {
                Log.e("uploadExercise", "Erro ao fazer upload do exercício", e)
                message = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteExercise(exerciseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                repository.deleteExercise(exerciseId)
            } catch (e: Exception) {
                Log.e("uploadExercise", "Erro ao fazer upload do exercício", e)
                message = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getTraining(trainingId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                trainingState = repository.getTraining(trainingId)
            } catch (e: Exception) {
                Log.e("uploadExercise", "Erro ao fazer upload do exercício", e)
                message = e.message
            } finally {
                isLoading = false
            }
        }
    }

    // Função para buscar todos os exercícios de um treino específico
    fun getExercisesByTrainingId(trainingId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                exercises = repository.getExercisesByTrainingId(trainingId)
            } catch (e: Exception) {
                message = e.message
            } finally {
                isLoading = false
            }
        }

    }

}