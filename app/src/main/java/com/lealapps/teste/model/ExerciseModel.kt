package com.lealapps.teste.model

import java.util.UUID

data class ExerciseModel(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var comment: String,
    var image: String,
    var trainingId: String,
) {
    constructor() : this(
        id = UUID.randomUUID().toString(),
        name = "",
        comment = "",
        image = "",
        trainingId = ""
    )
}