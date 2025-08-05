package com.lealapps.teste.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lealapps.teste.ui.auth.Login
import com.lealapps.teste.ui.auth.SignUp
import com.lealapps.teste.ui.exercise.CreateExercise
import com.lealapps.teste.ui.exercise.EditExercise
import com.lealapps.teste.ui.exercise.HomeExercises
import com.lealapps.teste.ui.exercise.ViewExercise
import com.lealapps.teste.ui.home.HomeScreen
import com.lealapps.teste.ui.profile.ProfileScreen
import com.lealapps.teste.ui.training.CreateTraining
import com.lealapps.teste.ui.training.HomeTraining
import com.lealapps.teste.ui.training.UpdateTraining
import com.lealapps.teste.viewmodel.ExerciseViewModel
import com.lealapps.teste.viewmodel.TrainingViewModel
import com.lealapps.teste.viewmodel.UserViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()
    val trainingViewModel: TrainingViewModel = viewModel()

    val currentUser = userViewModel.user

    NavHost(
        navController,
        startDestination = if (currentUser == null) Routes.LOGIN else Routes.HOME
    ) {
        composable(Routes.LOGIN) {
            Login(
                viewModel = userViewModel,
                navController = navController
            )
        }

        composable(Routes.SIGN_UP) {
            SignUp(
                userViewModel = userViewModel,
                navController = navController,
            )
        }

        composable(Routes.HOME) {
            if (currentUser != null) {
                HomeScreen(
                    navController = navController,
                    currentUser = currentUser,
                )
            } else {
                navController.navigate(Routes.LOGIN)
            }

        }

        composable(Routes.TRAINING) {
            if (currentUser != null) {
                HomeTraining(
                    navHostController = navController,
                    trainingViewModel
                )
            } else {
                navController.navigate(Routes.LOGIN)
            }

        }

        composable(Routes.PROFILE) {
            if (currentUser != null) {
                ProfileScreen(
                    navController,
                    userViewModel
                )
            } else {
                navController.navigate(Routes.LOGIN)
            }

        }

        composable(Routes.CREATE_TRAINING) {
            if (currentUser != null) {
                CreateTraining(
                    navController = navController,
                    viewModel = trainingViewModel
                )
            } else navController.navigate(Routes.LOGIN)
        }
        composable(Routes.EDIT_TRAINING) {
            if (currentUser != null) {
                UpdateTraining(
                    viewModel = trainingViewModel,
                    navController = navController,
                )
            } else navController.navigate(Routes.LOGIN)
        }

        composable("${Routes.HOME_EXERCISE}/{documentPath}") { backStackEntry ->
            if (currentUser != null) {
                val documentPath =
                    backStackEntry.arguments?.getString("documentPath")
                HomeExercises(
                    trainingId = documentPath,
                    navController = navController,
                    viewModel = exerciseViewModel
                )
            } else navController.navigate(Routes.LOGIN)

        }

        composable(Routes.CREATE_EXERCISE) {
            if (currentUser != null) {

                CreateExercise(
                    viewModel = exerciseViewModel,
                    navController = navController,
                )
            } else navController.navigate(Routes.LOGIN)
        }

        composable(Routes.VIEW_EXERCISE) {
            if (currentUser != null) {
                ViewExercise(
                    viewModel = exerciseViewModel,
                    navController = navController
                )
            } else navController.navigate(Routes.LOGIN)

        }

        composable(Routes.EDIT_EXERCISE) {
            if (currentUser != null) {
                EditExercise(
                    viewModel = exerciseViewModel,
                    navController = navController,
                )
            } else navController.navigate(Routes.LOGIN)

        }

    }
}


object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signUp"
    const val HOME = "home"
    const val TRAINING = "training"
    const val EDIT_TRAINING = "editTraining"
    const val PROFILE = "profile"
    const val CREATE_TRAINING = "createTraining"
    const val HOME_EXERCISE = "homeExercises"
    const val CREATE_EXERCISE = "createExercise"
    const val VIEW_EXERCISE = "viewExercise"
    const val EDIT_EXERCISE = "editExercise"
}