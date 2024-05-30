package com.lealapps.teste.models

import java.util.Date // Importe a classe java.util.Date

data class TrainingModel (
    var id: String? = null,
    var name: String? = null,
    var comment: String? = null,
    var date: Date = Date(), // Altere o tipo para java.util.Date
    var exercises: MutableList<ExerciseModel>? = mutableListOf()
) {
    fun addExersice(exercise: ExerciseModel) {
        exercises?.add(exercise)
    }
}
