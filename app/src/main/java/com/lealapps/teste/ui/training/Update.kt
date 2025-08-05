package com.lealapps.teste.ui.training

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.ui.components.AppLayout
import com.lealapps.teste.ui.components.BottomBar
import com.lealapps.teste.viewmodel.TrainingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTraining(
    viewModel: TrainingViewModel,
    navController: NavHostController
) {
    val training = viewModel.trainings.find {
        it.id == viewModel.trainingId
    }

    val colors = MaterialTheme.colorScheme
    val spacing = 16.dp

    AppLayout(
        title = "Editar Treino",
        selectedIcon = BottomBar.TRAINING.value,
        navigateToHome = { navController.navigate(Routes.HOME) },
        navigateToProfile = { navController.navigate(Routes.PROFILE) }
    ) { _, _ ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(horizontal = spacing, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ElevatedCard(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, colors.outline),
                        shape = RoundedCornerShape(16.dp)
                    ),
                onClick = { /* opcional */ }
            ) {
                Column(
                    modifier = Modifier.padding(spacing),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Filled.SportsMma,
                        contentDescription = "Ícone de treino",
                        tint = colors.primary,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(bottom = spacing)
                    )

                    OutlinedTextField(
                        value = viewModel.nameTraining,
                        onValueChange = { viewModel.nameTraining = it },
                        label = { Text("Nome do treino") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.outline,
                            cursorColor = colors.primary,
                            focusedLabelColor = colors.primary,
                            unfocusedLabelColor = colors.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(spacing))

                    OutlinedTextField(
                        value = viewModel.trainingObservations,
                        onValueChange = { viewModel.trainingObservations = it },
                        label = { Text("Descrição") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.outline,
                            cursorColor = colors.primary,
                            focusedLabelColor = colors.primary,
                            unfocusedLabelColor = colors.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(spacing))

                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = colors.outline
                    )

                    Spacer(modifier = Modifier.height(spacing))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = { navController.popBackStack() }) {
                            Text(
                                text = "Cancelar",
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (
                            viewModel.nameTraining.isNotBlank() &&
                            viewModel.trainingObservations.isNotBlank()
                        ) {
                            TextButton(
                                onClick = {
                                    training?.id?.let {
                                        viewModel.updateTraining(documentPath = it)
                                    }
                                    navController.popBackStack()
                                }
                            ) {
                                Text(
                                    text = "Editar",
                                    color = colors.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
