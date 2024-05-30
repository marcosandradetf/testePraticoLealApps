package com.lealapps.teste.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.lealapps.teste.models.TrainingModel
import com.lealapps.teste.ui.components.CardOption
import com.lealapps.teste.api.ExerciseViewModel
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(
    user: String,
    navHostController: NavHostController,
    viewModel: ExerciseViewModel
) {
    // Usando um estado para armazenar se a coleção existe ou não
    val collectionExists = remember { mutableStateOf(false) }
    val showProgress = remember { mutableStateOf(true) }
    var workouts by remember { mutableStateOf<List<TrainingModel>>(emptyList()) }
    val db = Firebase.firestore

    LaunchedEffect(Unit) {
        try {
            val result = db.collection("training").get().await()
            // Verifica se a coleção possui documentos
            collectionExists.value = !result.isEmpty

            if (!result.isEmpty) {
                val workoutsList = mutableListOf<TrainingModel>()
                for (document in result.documents) {
                    val trainingModel = document.toObject<TrainingModel>()
                    trainingModel?.id = document.id // Defina o ID do documento no objeto
                    if (trainingModel != null) {
                        workoutsList.add(trainingModel)
                    }
                }
                workouts = workoutsList
            }
        } catch (e: FirebaseFirestoreException) {
            // Lidar com erros de acesso ao Firestore
            Log.e(TAG, "Erro ao acessar a coleção training", e)
        } finally {
            showProgress.value = false
        }
    }

    if (showProgress.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
        }
    } else {
        viewModel.clearFieldsTraining()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Olá $user, seja bem-vindo",
                        color = Color.White,
                        fontSize = 18.sp
                    )
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
                        viewModel = viewModel)

                } else {
                    Row {
                        CardOption(
                            imagem = "https://img.freepik.com/fotos-gratis/mulher-com-abdomen-visivel-fazendo-fitness_23-2150228944.jpg?t=st=1716672428~exp=1716676028~hmac=73a7dba18425d19a4c039db39f7be68a3cdc1f542a53dee4ef5be213934a79d9&w=1380",
                            desc = "Acessar treinos",
                            customColor = Color(0xFFFF593F),
                            route = { navHostController.navigate("homeTraining") }
                        )
                        CardOption(
                            imagem = "https://img.freepik.com/fotos-gratis/mulher-com-abdomen-visivel-fazendo-fitness_23-2150228944.jpg?t=st=1716672428~exp=1716676028~hmac=73a7dba18425d19a4c039db39f7be68a3cdc1f542a53dee4ef5be213934a79d9&w=1380",
                            desc = "Acessar exercícios",
                            customColor = Color.Black,
                            route = { navHostController.navigate("homeExercise") }
                        )

                    }
                }
            }
        }
    }


}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadTraining(workouts: List<TrainingModel>,
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
                    navHostController.navigate("view") },
                colors = CardDefaults.cardColors(Color(0xFF21252B))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp) // Adiciona padding para espaçamento entre os itens
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
                        training.comment?.let { Text(text = it, color = Color(0xFFD8D8D8)) }
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
                                AlertDialogExample(
                                    onDismissRequest = { openAlertDialog.value = false },
                                    onConfirmation = {
                                        openAlertDialog.value = false
                                        println("Confirmation registered") // Add logic here to handle confirmation.
                                    },
                                    dialogTitle = "Alert dialog example",
                                    dialogText = "This is an example of an alert dialog with buttons.",
                                    icon = Icons.Filled.Info
                                )
                            }
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                      navHostController.navigate("editTraining")
                            },
                            containerColor = Color(0xFF5B90FE),
                            contentColor = Color.White,
                            shape = CircleShape
                        ) {
                            Icon(Icons.Filled.Edit, "Edit Training")
                        }
                        SmallFloatingActionButton(
                            onClick = { openAlertDialog.value = true
                                training.id?.let {
                                    viewModel.deleteTraining(
                                        it
                                    )
                                }},
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {

                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}