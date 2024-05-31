package com.lealapps.teste.ui.exercise

import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lealapps.teste.api.ExerciseViewModel
import com.lealapps.teste.models.ExerciseModel
import com.lealapps.teste.ui.components.DeleteDialog
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewExercise(
    viewModel: ExerciseViewModel,
    navController: NavController
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    viewModel.clearFieldsExercise()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "${viewModel.exerciseState?.name}") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("homeExercises") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .width(width = 375.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            Color(0xFF54575C)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
                onClick = { navController.navigate("editExercise") },
                colors = CardDefaults.cardColors(Color(0xFF21252B))
            ) {
                Row {
                    Column(
                        Modifier.height(height = 250.dp)
                    ) {

                        if (viewModel.exerciseState?.image != "null") {
                            // Exiba a imagem se estiver disponível
                            AsyncImage(
                                model = viewModel.exerciseState?.image,
                                contentDescription = "Image"
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(width = 150.dp, height = 250.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFF70777C),
                                        shape = RoundedCornerShape(5.dp)
                                    ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AddAPhoto,
                                        contentDescription = "Selecionar Imagem",
                                        modifier = Modifier.size(width = 50.dp, height = 50.dp)
                                    )
                                    Text(text = "Nenhuma Imagem", fontSize = 12.sp)
                                }

                            }
                        }

                    }

                    Spacer(
                        modifier = Modifier
                            .size(1.dp, 200.dp)
                            .border(
                                BorderStroke(1.dp, Color(0xFF54575C))
                            )
                    )


                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, bottom = 20.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            viewModel.exerciseState?.name?.let { it1 -> Text(text = it1) }
                        }

                        Row {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .border(
                                        BorderStroke(1.dp, Color(0xFF54575C))
                                    )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ElevatedCard(
                                colors = CardDefaults.cardColors(Color(0xFFF9A825)),
                                elevation = CardDefaults.elevatedCardElevation(10.dp),
                                modifier = Modifier
                                    .size(
                                        width = 150.dp,
                                        height = 100.dp
                                    )
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                    text = "${viewModel.exerciseState?.comment}",
                                    fontSize = 15.sp,
                                    lineHeight = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            SmallFloatingActionButton(
                                onClick = {
                                    navController.navigate("editExercise")
                                },
                                containerColor = Color(0xFF5B90FE),
                                contentColor = Color.White,
                                shape = CircleShape
                            ) {
                                Icon(Icons.Filled.Edit, "Edit Training")
                            }
                            SmallFloatingActionButton(
                                onClick = {
                                    openAlertDialog.value = true },
                                containerColor = Color(0xFFF1526D),
                                contentColor = Color.White,
                                shape = CircleShape
                            ) {
                                Icon(Icons.Filled.Delete, "Delete Training")
                                when {
                                    openAlertDialog.value -> {
                                        DeleteDialog(
                                            onDismissRequest = { openAlertDialog.value = false },
                                            onConfirmation = {
                                                openAlertDialog.value = false
                                                viewModel.trainingState?.id?.let { it1 ->
                                                    viewModel.exerciseState?.id?.let { it2 ->
                                                        viewModel.deleteExercise(
                                                            documentPath = it1,
                                                            exerciseIndex = it2
                                                        )
                                                    }
                                                }
                                                navController.navigate("home")},
                                            dialogTitle = "Exluir exercício",
                                            dialogText = "Ao confirmar o exercício será excluído",
                                            icon = Icons.Filled.Info,
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