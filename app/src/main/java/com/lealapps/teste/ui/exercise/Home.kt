package com.lealapps.teste.ui.exercise

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lealapps.teste.api.ExerciseViewModel
import com.lealapps.teste.models.TrainingModel
import com.lealapps.teste.ui.components.FirstCreate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeExercises(
    training: TrainingModel?,
    navController: NavHostController,
    viewModel: ExerciseViewModel
    ) {

    viewModel.clearFieldsExercise()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (training != null) {
                        Text(
                            text = "${training.name}"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("createExercise") },
                containerColor = Color.Green,
                contentColor = Color.Black
            ) {
                Icon(
                    Icons.Filled.Add,
                    "Large floating action button",
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color(0xFF001F24)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (training != null) {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    training.exercises?.let { exercises ->
                        items(exercises.size) { index ->

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
                                    onClick = { viewModel.exerciseState = exercises[index]
                                        navController.navigate("viewExercise") }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(10.dp)
                                    ) {

                                        if(exercises[index].image != null) {
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
                                                    model = exercises[index].image,
                                                    contentDescription = "Exercise Image"
                                                )
                                            }
                                        } else {
                                            Box(
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
                                                Icon(imageVector = Icons.Filled.HideImage, contentDescription = "Not Image")
                                            }
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "${exercises[index].name}",
                                                fontFamily = FontFamily.SansSerif
                                            )
                                        }
                                    }
                                }

                        }
                    }
                }

                if (training.exercises?.size == 0) {
                    FirstCreate(
                        navController = navController,
                        route = "createExercise",
                        label = "Adicionar primeiro exercício"
                    )
                }
            }
        }
    }
}