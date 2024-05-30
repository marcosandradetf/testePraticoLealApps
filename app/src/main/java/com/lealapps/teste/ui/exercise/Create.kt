package com.lealapps.teste.ui.exercise

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lealapps.teste.api.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExercise(
    viewModel: ExerciseViewModel,
    pickMedia: ActivityResultLauncher<PickVisualMediaRequest>,
    selectedImageUri: Uri?,
    navigateBack: () -> Unit,
) {
    Column {
        ElevatedCard(onClick = { /*TODO*/ }) {
            Column {
                if ((viewModel.nameExercise.value
                        .isEmpty() || viewModel.commExercise.value.isEmpty() || selectedImageUri?.path.isNullOrEmpty())
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
                        model = selectedImageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 280.dp, height = 150.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF70777C),
                                shape = RoundedCornerShape(5.dp)
                            ),
                    )

                    ElevatedButton(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .width(280.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black),
                        onClick = {
                            // Verifica se selectedImageUri não é nulo antes de chamar uploadToFirebaseStorage
                            selectedImageUri?.let { uri ->
                                viewModel.uploadMedia(
                                    uri = uri
                                )
                            }
                        }
                    ) {
                        Text("Criar exercício")
                    }
                }
            }
        }
    }
}