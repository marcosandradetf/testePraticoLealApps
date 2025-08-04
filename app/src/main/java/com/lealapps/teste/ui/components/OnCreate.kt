package com.lealapps.teste.ui.components

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.lealapps.teste.viewmodel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnCreate(
    viewModel: ExerciseViewModel,
    pickMedia: ActivityResultLauncher<PickVisualMediaRequest>,
    selectedImageUri: Uri?,
    navigateBack: () -> Unit,
) {

}