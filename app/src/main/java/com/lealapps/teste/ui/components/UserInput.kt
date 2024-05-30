package com.lealapps.teste.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import com.lealapps.teste.api.ExerciseViewModel

@Composable
fun UserInput(label: String, type: String, viewModel: ExerciseViewModel, lines: Int) {

    if (type == "text") {
        OutlinedTextField(
            minLines = lines,
            value = viewModel.commExercise.value,
            onValueChange = { viewModel.setCommExercise(it) },
            label = { Text(text = label) },
        )
    } else if (lines > 1) {
        OutlinedTextField(
            minLines = lines,
            value = viewModel.commExercise.value,
            onValueChange = { viewModel.setCommExercise(it) },
            label = { Text(text = label) },
        )
    }
}