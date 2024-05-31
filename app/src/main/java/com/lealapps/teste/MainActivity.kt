package com.lealapps.teste

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

import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lealapps.teste.ui.auth.Login
import com.lealapps.teste.ui.theme.TesteTheme
import com.lealapps.teste.ui.training.CreateTraining
import com.lealapps.teste.api.ExerciseViewModel
import com.lealapps.teste.ui.exercise.CreateExercise
import com.lealapps.teste.ui.exercise.EditExercise
import com.lealapps.teste.ui.home.HomeActivity
import com.lealapps.teste.ui.exercise.HomeExercises
import com.lealapps.teste.ui.exercise.ViewExercise
import com.lealapps.teste.ui.training.UpdateTraining

class MainActivity : ComponentActivity() {
    private val viewModel: ExerciseViewModel by viewModels()
    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            viewModel.selectedImageUri = uri
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
                            CreateTraining(
                                navigateBack = {
                                    navController.popBackStack()
                                },
                                viewModel = viewModel
                            )
                        }
                        composable("editTraining") {
                            UpdateTraining(
                                viewModel = viewModel,
                                training = viewModel.trainingState,
                                navController = navController,
                            )
                        }

                        composable("homeExercises") {
                            HomeExercises(
                                training = viewModel.trainingState,
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                        composable("createExercise") {
                            CreateExercise(
                                viewModel = viewModel,
                                pickMedia = pickMedia,
                                navigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("viewExercise") {
                            ViewExercise(
                                viewModel = viewModel,
                                navController = navController
                            )
                        }

                        composable("editExercise") {
                            EditExercise(
                                viewModel = viewModel,
                                navController = navController,
                                pickMedia = pickMedia
                            )
                        }

                    }
                }
            }
        }
    }
}
