package com.lealapps.teste.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lealapps.teste.api.ExerciseViewModel

@Composable
fun UserInput(label: String, type: String, viewModel: ExerciseViewModel, lines: Int) {

    if (lines == 1) {
        OutlinedTextField(
            minLines = lines,
            value = viewModel.nameTraining.value,
            onValueChange = { viewModel.setNameTraining(it) },
            label = { Text(text = label) },
        )
    } else if (lines > 1) {
        OutlinedTextField(
            minLines = lines,
            value = viewModel.commTraining.value,
            onValueChange = { viewModel.setCommTraining(it) },
            label = { Text(text = label) },
        )
    }
}