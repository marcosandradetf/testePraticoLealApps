package com.lealapps.teste.ui.training

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lealapps.teste.model.TrainingModel
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.ui.components.AppLayout
import com.lealapps.teste.ui.components.BottomBar
import com.lealapps.teste.ui.components.DeleteDialog
import com.lealapps.teste.ui.components.FirstCreate
import com.lealapps.teste.viewmodel.TrainingViewModel
import java.text.SimpleDateFormat


@Composable
fun HomeTraining(
    navHostController: NavHostController,
    viewModel: TrainingViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.getAll()
    }

    AppLayout(
        title = "Treinos",
        selectedIcon = BottomBar.TRAINING.value,
        navigateToHome = { navHostController.navigate(Routes.HOME) },
        navigateToProfile = { navHostController.navigate(Routes.PROFILE) }
    ) { modifier, showSnackBar ->
        if (viewModel.message != null) {
            showSnackBar(viewModel.message ?: "", null)
            viewModel.message = null
        }

        if (viewModel.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.trainings.isNotEmpty()) {
                    LoadTraining(
                        workouts = viewModel.trainings,
                        navHostController = navHostController,
                        viewModel = viewModel
                    )

                    TextButton(
                        onClick = { navHostController.navigate(Routes.CREATE_TRAINING) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Adicionar treino",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else if (viewModel.message != null) {
                    Text(viewModel.message ?: "")
                } else {
                    FirstCreate(
                        navController = navHostController,
                        route = "createTraining",
                        label = "Criar primeiro treino"
                    )
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadTraining(
    workouts: List<TrainingModel>,
    navHostController: NavHostController,
    viewModel: TrainingViewModel
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val colors = MaterialTheme.colorScheme

    LazyColumn {
        items(workouts) { training ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .width(300.dp)
                    .padding(5.dp)
                    .border(
                        BorderStroke(1.dp, colors.outline),
                        shape = RoundedCornerShape(12.dp)
                    ),
                onClick = {
                    navHostController.navigate("${Routes.HOME_EXERCISE}/${training.id}")
                },
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = training.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onSurface
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = "Data",
                            tint = colors.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = " ${sdf.format(training.date)}",
                            color = colors.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }

                    Divider(
                        thickness = 1.dp,
                        color = colors.outline
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = training.comment,
                            color = colors.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Filled.SportsGymnastics,
                            contentDescription = "Exercícios",
                            tint = colors.primary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (openAlertDialog.value) {
                            DeleteDialog(
                                onDismissRequest = { openAlertDialog.value = false },
                                onConfirmation = {
                                    openAlertDialog.value = false
                                    viewModel.deleteTraining()
                                },
                                dialogTitle = "Excluir treino",
                                dialogText = "Ao confirmar o treino será excluído",
                                icon = Icons.Filled.Info
                            )
                        }

                        SmallFloatingActionButton(
                            onClick = {
                                viewModel.trainingId = training.id
                                viewModel.nameTraining = training.name
                                viewModel.trainingObservations = training.comment
                                navHostController.navigate(Routes.EDIT_TRAINING)
                            },
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary,
                            shape = CircleShape
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }

                        SmallFloatingActionButton(
                            onClick = {
                                viewModel.trainingId = training.id
                                openAlertDialog.value = true
                            },
                            containerColor = colors.error,
                            contentColor = colors.onError,
                            shape = CircleShape
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Excluir")
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun PrevHome() {
    val viewModel: TrainingViewModel = viewModel()
    HomeTraining(
        navHostController = rememberNavController(),
        viewModel = viewModel,
    )
}