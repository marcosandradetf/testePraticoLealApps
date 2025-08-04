package com.lealapps.teste.model

import java.util.Date
import java.util.UUID

data class TrainingModel (
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var comment: String,
    var date: Date = Date(),
) {
    constructor() : this(
        id = UUID.randomUUID().toString(),
        name = "",
        comment = "",
        date = Date()
    )
}
