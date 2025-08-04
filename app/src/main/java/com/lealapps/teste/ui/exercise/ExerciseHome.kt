package com.lealapps.teste.ui.exercise

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.ui.components.AppLayout
import com.lealapps.teste.ui.components.BottomBar
import com.lealapps.teste.ui.components.FirstCreate
import com.lealapps.teste.viewmodel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeExercises(
    trainingId: String?,
    navController: NavHostController,
    viewModel: ExerciseViewModel
) {

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (trainingId != null) {
            isLoading = true
            viewModel.getTraining(trainingId)
            viewModel.getExercisesByTrainingId(trainingId)
            isLoading = false
        }
    }

    AppLayout(
        title = viewModel.trainingState?.name ?: "",
        selectedIcon = BottomBar.TRAINING.value,
        navigateBack = {
            navController.popBackStack()
        },
        navigateToHome = {
            navController.navigate(Routes.HOME)
        },
        navigateToTraining = {
            navController.navigate(Routes.TRAINING)
        },
        navigateToProfile = { navController.navigate(Routes.PROFILE) }
    ) { modifier, _ ->

        if (isLoading)
            Column(
                modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        else

            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val training = viewModel.trainingState
                if (training != null) {

                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(viewModel.exercises) { exercise ->

                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .border(
                                        BorderStroke(
                                            1.dp,
                                            Color(0xFF54575C)
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                onClick = {
                                    viewModel.exerciseState = exercise
                                    navController.navigate("viewExercise")
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                ) {

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(
                                                width = 200.dp,
                                                height = 150.dp
                                            )
                                            .border(
                                                BorderStroke(
                                                    1.dp, Color.White
                                                )
                                            )
                                    ) {
                                        AsyncImage(
                                            model = exercise.image,
                                            contentDescription = "Exercise Image"
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = exercise.name,
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    }
                                }
                            }

                        }

                    }

                    if (viewModel.exercises.isEmpty()) {
                        FirstCreate(
                            navController = navController,
                            route = Routes.CREATE_EXERCISE,
                            label = "Adicionar primeiro exercício"
                        )
                    }
                }
            }
    }
}

