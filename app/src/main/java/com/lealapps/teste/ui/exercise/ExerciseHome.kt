package com.lealapps.teste.ui.exercise

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.ui.components.AppLayout
import com.lealapps.teste.ui.components.BottomBar
import com.lealapps.teste.ui.components.FirstCreate
import com.lealapps.teste.viewmodel.ExerciseViewModel

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeExercises(
    trainingId: String?,
    navController: NavHostController,
    viewModel: ExerciseViewModel
) {
    LaunchedEffect(trainingId) {
        if (trainingId != null) {
            viewModel.isLoading = true
            viewModel.getTraining(trainingId)
            viewModel.getExercisesByTrainingId(trainingId)
            viewModel.isLoading = false
        }
    }

    AppLayout(
        title = viewModel.trainingState?.name ?: "",
        selectedIcon = BottomBar.TRAINING.value,
        navigateBack = { navController.popBackStack() },
        navigateToHome = { navController.navigate(Routes.HOME) },
        navigateToTraining = { navController.navigate(Routes.TRAINING) },
        navigateToProfile = { navController.navigate(Routes.PROFILE) }
    ) { modifier, showSnackBar ->

        if (viewModel.message != null) {
            showSnackBar(viewModel.message ?: "", null)
            viewModel.message = null
        }

        if (viewModel.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val training = viewModel.trainingState
            if (training != null) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    if (viewModel.exercises.isEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        FirstCreate(
                            navController = navController,
                            route = Routes.CREATE_EXERCISE,
                            label = "Adicionar primeiro exercício"
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(viewModel.exercises) { exercise ->
                                ElevatedCard(
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(6.dp),
                                    onClick = {
                                        viewModel.exerciseState = exercise
                                        navController.navigate(Routes.VIEW_EXERCISE)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(0.85f)
                                        .animateItemPlacement()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        AsyncImage(
                                            model = exercise.image,
                                            contentDescription = "Imagem do exercício",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(120.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = exercise.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.fillMaxWidth(),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            item {
                                ElevatedCard(
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    onClick = {
                                        navController.navigate(Routes.CREATE_EXERCISE)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(0.85f)
                                        .animateItemPlacement(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF5B90FE)) // Azul vibrante
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.AddCircle,
                                            contentDescription = "Adicionar Exercício",
                                            tint = Color.White,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Adicionar",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            maxLines = 1,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
