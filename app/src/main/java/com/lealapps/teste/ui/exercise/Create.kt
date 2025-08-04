package com.lealapps.teste.ui.exercise

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.ui.components.AppLayout
import com.lealapps.teste.ui.components.BottomBar
import com.lealapps.teste.viewmodel.ExerciseViewModel

@Composable
fun CreateExercise(
    viewModel: ExerciseViewModel,
    navController: NavController,
) {
    viewModel.clearFieldsExercise()

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.selectedImageUri = uri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    var isFirstComposition by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel.exercises.size) {
        // Só navega se não for a primeira composição
        if (!isFirstComposition && viewModel.trainingState != null) {
            navController.navigate("${Routes.HOME_EXERCISE}/${viewModel.trainingState!!.id}")
        }

        // Marca a primeira composição como concluída
        isFirstComposition = false
    }


    AppLayout(
        title = "Novo exercício",
        selectedIcon = BottomBar.TRAINING.value,
        navigateBack = {
            navController.popBackStack()
        },
        navigateToHome = {
            navController.navigate(Routes.HOME)
        },
        navigateToProfile = {
            navController.navigate(Routes.PROFILE)
        }
    ) { modifier, showSnackBar ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ElevatedCard(
                colors = CardDefaults.cardColors(Color(0xFF282C34)),
                modifier = Modifier
                    .padding(10.dp)
                    .border(
                        BorderStroke(1.dp, Color(0xFF606368)),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SportsGymnastics,
                        contentDescription = "Insert Training",
                        modifier = Modifier.size(50.dp),
                        tint = Color.Green
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        minLines = 1,
                        value = viewModel.nameExercise,
                        onValueChange = { viewModel.nameExercise = it },
                        label = { Text(text = "Nome do exercício") },
                    )

                    OutlinedTextField(
                        minLines = 4,
                        value = viewModel.exerciseObservations,
                        onValueChange = { viewModel.exerciseObservations = it },
                        label = { Text(text = "Observações") },
                    )

                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    if (viewModel.selectedImageUri?.path.isNullOrEmpty()
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                }
                                .size(width = 280.dp, height = 150.dp)
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
                                Text(text = "Selecionar Imagem")
                            }

                        }
                    } else {
                        AsyncImage(
                            model = viewModel.selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(width = 280.dp, height = 150.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF70777C),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .clickable {
                                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                },
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .border(BorderStroke(1.dp, color = Color(0xFF54575C)))
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = { navController.navigate("home") },
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Text(
                            text = "CANCELAR",
                            color = Color(0xFF5B90FE),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }

                    if (
                        (viewModel.nameExercise.isNotEmpty() && viewModel.exerciseObservations.isNotEmpty()) && !viewModel.selectedImageUri?.path.isNullOrEmpty()
                    ) {
                        Spacer(
                            modifier = Modifier
                                .size(1.dp, 50.dp)
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        Color(0xFF54575C)
                                    )
                                )
                        )

                        Button(
                            onClick = {
                                if (viewModel.selectedImageUri != null && viewModel.trainingState != null) {
                                    viewModel.uploadExercise(viewModel.trainingState!!.id)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent)
                        ) {
                            Text(
                                text = "CRIAR EXERCÍCIO",
                                color = Color(0xFF5B90FE),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun PrevCreateExercise() {
    CreateExercise(
        viewModel = ExerciseViewModel(),
        navController = rememberNavController()
    )
}