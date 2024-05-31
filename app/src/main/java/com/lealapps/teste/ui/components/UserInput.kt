package com.lealapps.teste.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lealapps.teste.api.ExerciseViewModel

@Composable
fun UserInput(
    label: String,
    training: Boolean? = null,
    viewModel: ExerciseViewModel,
    lines: Int
) {

    if (training == true) {
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
    } else {
        if (lines == 1) {
            OutlinedTextField(
                minLines = lines,
                value = viewModel.nameExercise.value,
                onValueChange = { viewModel.setNameExercise(it) },
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

}