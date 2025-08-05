package com.lealapps.teste.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lealapps.teste.firebase.FirebaseService.translateFirebaseError
import com.lealapps.teste.model.ExerciseModel
import com.lealapps.teste.model.TrainingModel
import com.lealapps.teste.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ExerciseViewModel : ViewModel() {
    private val repository = ExerciseRepository()

    var isLoading by mutableStateOf(false)
    var updated by mutableStateOf(false)
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
        viewModelScope.launch {
            if (selectedImageUri != null) {
                isLoading = true
                try {
                    val exercise = ExerciseModel(
                        id = UUID.randomUUID().toString(),
                        name = nameExercise,
                        comment = exerciseObservations,
                        image = "",
                        trainingId = trainingId
                    )
                    val imageUri =
                        withContext(Dispatchers.IO) {
                            repository.uploadExercise(selectedImageUri!!, exercise)
                        }

                    exercises = exercises + exercise.copy(image = imageUri)
                    message = "Exercício salvo com sucesso"
                    updated = true
                } catch (e: Exception) {
                    message = translateFirebaseError(e)
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun updateExercise(exerciseId: String, trainingId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val exercise = ExerciseModel(
                    id = exerciseId,
                    name = nameExercise,
                    comment = exerciseObservations,
                    image = exerciseState?.image ?: "", // imagem será atualizada
                    trainingId = trainingId
                )

                val newImageUri = if (selectedImageUri != null) {
                    withContext(Dispatchers.IO) {
                        repository.updateExercise(exerciseId, exercise, selectedImageUri!!)
                    }
                } else {
                    exercise.image // mantém a imagem antiga se não alterar
                }

                exerciseState = exerciseState?.copy(
                    name = nameExercise,
                    comment = exerciseObservations,
                    image = newImageUri
                )
                updated = true
                message = "Exercício atualizado com sucesso"

            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }


    fun deleteExercise(exerciseId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    repository.deleteExercise(exerciseId)
                }
                updated = true
                message = "Exercício excluído com sucesso"
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }


    fun getTraining(trainingId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val training = withContext(Dispatchers.IO) {
                    repository.getTraining(trainingId)
                }
                trainingState = training
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }


    fun getExercisesByTrainingId(trainingId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getExercisesByTrainingId(trainingId)
                }
                exercises = result
            } catch (e: Exception) {
                message = translateFirebaseError(e)
            } finally {
                isLoading = false
            }
        }
    }


}