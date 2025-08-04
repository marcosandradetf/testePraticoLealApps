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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.ui.components.AppLayout
import com.lealapps.teste.ui.components.BottomBar
import com.lealapps.teste.viewmodel.TrainingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTraining(
    navController: NavHostController,
    viewModel: TrainingViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.clearFieldsTraining()
    }

    AppLayout(
        title = "Novo Treino",
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
        navigateToProfile = {
            navController.navigate(Routes.PROFILE)
        }
    ) {
        modifier, showSnackBar ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ElevatedCard(
                        onClick = { /*TODO*/ },
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
                                .background(Color.Green)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SportsMma,
                                contentDescription = "Insert Training",
                                modifier = Modifier.size(50.dp),
                                tint = Color(0xFF21252B)
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            OutlinedTextField(
                                minLines = 1,
                                value = viewModel.nameTraining,
                                onValueChange = { viewModel.nameTraining = it },
                                label = { Text(text = "Nome do treino") },
                            )

                            OutlinedTextField(
                                minLines = 4,
                                value = viewModel.trainingObservations,
                                onValueChange = { viewModel.trainingObservations = it },
                                label = { Text(text = "Descrição") },
                            )


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
                                onClick = { navController.popBackStack() },
                                colors = ButtonDefaults.buttonColors(Color.Transparent)
                            ) {
                                Text(
                                    text = "CANCELAR",
                                    color = Color(0xFF5B90FE),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }

                            if ((viewModel.nameTraining.isNotEmpty() && viewModel.trainingObservations.isNotEmpty())
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
                                        viewModel.uploadTraining()
                                        navController.popBackStack()
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                                ) {
                                    Text(
                                        text = "SALVAR",
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


}

@Composable
@Preview
fun PrevCreateT() {
    CreateTraining(
        navController = rememberNavController(),
        viewModel = TrainingViewModel()
    )
}