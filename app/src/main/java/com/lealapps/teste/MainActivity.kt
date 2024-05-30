package com.lealapps.teste

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lealapps.teste.ui.auth.Login
import com.lealapps.teste.ui.theme.TesteTheme
import com.lealapps.teste.ui.training.TrainingActivity
import com.lealapps.teste.api.ExerciseViewModel
import com.lealapps.teste.ui.home.HomeActivity
import com.lealapps.teste.ui.exercise.View

class MainActivity : ComponentActivity() {

    private var selectedImageUri by mutableStateOf<Uri?>(null)

    private val viewModel: ExerciseViewModel by viewModels()

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            selectedImageUri = uri
            Log.d("URI selectedImageUri da fun pickMedia", "$selectedImageUri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            val navController = rememberNavController()
            TesteTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController, startDestination = "home") {
                        composable("home") {
                            HomeActivity(
                                user = "Marcos",
                                navHostController = navController,
                                viewModel = viewModel
                            )
                        }
                        composable("signIn") {
                            Login()
                        }
                        composable("createTraining") {
                            TrainingActivity(
                                navigateBack = {
                                    navController.popBackStack()
                                },
                                viewModel = viewModel
                            )
                        }
                        composable("view") {
                            View(
                                training = viewModel.trainingState,
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}
