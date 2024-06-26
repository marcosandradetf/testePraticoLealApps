package com.lealapps.teste.ui.home

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lealapps.teste.models.TrainingModel
import com.lealapps.teste.api.ExerciseViewModel
import com.lealapps.teste.ui.components.DeleteDialog
import com.lealapps.teste.ui.components.FirstCreate
import java.text.SimpleDateFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(
    navHostController: NavHostController,
    viewModel: ExerciseViewModel,
    signOut: () -> Unit
) {
    // Usando um estado para armazenar se a coleção existe ou não
    val collectionExists = remember { mutableStateOf(false) }
    val showProgress = remember { mutableStateOf(true) }
    var workouts by remember { mutableStateOf<List<TrainingModel>>(emptyList()) }

    // chave usada para acionar o efeito LaunchedEffect
    val effectKey = remember { mutableStateOf(0) }

    LaunchedEffect(effectKey) {
        viewModel.getTraining(
            collectionExists = { result ->
                               collectionExists.value = result },
            setData = { result ->
                workouts = result  },
            disableLoading = { result ->
                showProgress.value = result }
        )
    }

    if (showProgress.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            effectKey.value++
        }
    } else {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Lista de treinos",
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            IconButton(
                                onClick = { signOut() },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Output,
                                    contentDescription = "User profile",
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            }
                        }

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF15172C))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navHostController.navigate("createTraining") },
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
                .background(color = Color(0xFF15172C)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                if (collectionExists.value) {
                    LoadTraining(
                        workouts = workouts,
                        navHostController = navHostController,
                        viewModel = viewModel,
                    )
                } else {
                    FirstCreate(
                        navController =navHostController,
                        route = "createTraining",
                        label = "Criar primeiro treino")
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadTraining(
    workouts:  List<TrainingModel>,
    navHostController: NavHostController,
    viewModel: ExerciseViewModel
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    LazyColumn {
        items(workouts) { training ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .width(width = 300.dp)
                    .padding(5.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            Color(0xFF54575C)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
                onClick = {
                    viewModel.updateTrainingState(training)
                    navHostController.navigate("homeExercises") },
                colors = CardDefaults.cardColors(Color(0xFF21252B))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Row{
                        Text(text = "${training.name}", color = Color(0xFFD8D8D8))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.AccessTime,
                            contentDescription = "Date",
                            tint = Color(0xFFD8D8D8),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = " ${sdf.format(training.date)}",
                            color = Color(0xFFD8D8D8),
                            fontSize = 12.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillParentMaxWidth()
                            .border(
                                border = BorderStroke(1.dp, Color(0xFF54575C)),
                            )
                    )
                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(top = 20.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        training.comment?.let {
                            Text(
                                modifier = Modifier.width(220.dp),
                                text = it,
                                color = Color(0xFFD8D8D8)
                            )
                        }
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color.Green,
                                    contentColor = Color(0xFFD8D8D8)
                                ) {
                                    Text("${training.exercises?.count()}", color = Color.Black)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SportsGymnastics,
                                contentDescription = "Exercises",
                                tint = Color(0xFFD8D8D8)
                            )
                        }

                    }
                    Row(
                        modifier = Modifier.width(300.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                        when {
                            // ...
                            openAlertDialog.value -> {
                                DeleteDialog(
                                    onDismissRequest = { openAlertDialog.value = false },
                                    onConfirmation = {
                                        openAlertDialog.value = false
                                        training.id?.let {
                                            viewModel.deleteTraining(it)
                                        }
                                        navHostController.navigate("home")},
                                    dialogTitle = "Exluir treino",
                                    dialogText = "Ao confirmar o treino será excluído",
                                    icon = Icons.Filled.Info,
                                )
                            }
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                viewModel.updateTrainingState(training)
                                navHostController.navigate("editTraining")
                            },
                            containerColor = Color(0xFF5B90FE),
                            contentColor = Color.White,
                            shape = CircleShape
                        ) {
                            Icon(Icons.Filled.Edit, "Edit Training")
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                viewModel.updateTrainingState(training)
                                openAlertDialog.value = true },
                            containerColor = Color(0xFFF1526D),
                            contentColor = Color.White,
                            shape = CircleShape
                        ) {
                            Icon(Icons.Filled.Delete, "Delete Training")
                        }
                    }
                }
            }
        }
    }
}