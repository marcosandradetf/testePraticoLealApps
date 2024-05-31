package com.lealapps.teste

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lealapps.teste.ui.auth.Login
import com.lealapps.teste.ui.theme.TesteTheme
import com.lealapps.teste.ui.training.CreateTraining
import com.lealapps.teste.api.ExerciseViewModel
import com.lealapps.teste.ui.auth.SignUp
import com.lealapps.teste.ui.exercise.CreateExercise
import com.lealapps.teste.ui.exercise.EditExercise
import com.lealapps.teste.ui.home.HomeActivity
import com.lealapps.teste.ui.exercise.HomeExercises
import com.lealapps.teste.ui.exercise.ViewExercise
import com.lealapps.teste.ui.training.UpdateTraining

class MainActivity : ComponentActivity() {
    private val viewModel: ExerciseViewModel by viewModels()

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                viewModel.selectedImageUri = uri
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        // Initialize Firebase Auth
        auth = Firebase.auth
        enableEdgeToEdge()
        installSplashScreen()


        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModel.setUserId(currentUser.uid)
        }
        setContent {
            val navController = rememberNavController()

            TesteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController,
                        startDestination = if (currentUser == null) "login" else "home"
                    ) {
                        composable("home") {
                            if (currentUser != null ) {
                                HomeActivity(
                                    navHostController = navController,
                                    viewModel = viewModel,
                                    signOut = {
                                        auth.signOut()
                                        navController.navigate("login")
                                    }
                                )
                            } else {
                                navController.navigate("login")
                            }

                        }
                        composable("login") {
                            Login(
                                sendCred = { email, password ->
                                    loginAccount(email, password, navController)
                                },
                                signUp = {
                                    navController.navigate("signUp")
                                }
                            )
                        }
                        composable("signUp") {
                            SignUp(
                                sendCred = { email, password, passwordConfirm ->
                                    createAccount(
                                        email,
                                        password,
                                        passwordConfirm,
                                        navController)
                                }
                            )
                        }
                        composable("createTraining") {
                            if (currentUser != null ) {
                                CreateTraining(
                                    navigateBack = {
                                        navController.popBackStack()
                                    },
                                    viewModel = viewModel
                                )
                            } else navController.navigate("login")
                        }
                        composable("editTraining") {
                            if (currentUser != null ) {
                                UpdateTraining(
                                    viewModel = viewModel,
                                    training = viewModel.trainingState,
                                    navController = navController,
                                )
                            } else navController.navigate("login")
                        }

                        composable("homeExercises") {
                            if (currentUser != null ) {
                                HomeExercises(
                                    training = viewModel.trainingState,
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            } else navController.navigate("login")

                        }
                        composable("createExercise") {
                            if (currentUser != null ) {
                                CreateExercise(
                                    viewModel = viewModel,
                                    pickMedia = pickMedia,
                                    navController = navController
                                )
                            } else navController.navigate("login")


                        }
                        composable("viewExercise") {
                            if (currentUser != null ) {
                                ViewExercise(
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            } else navController.navigate("login")

                        }

                        composable("editExercise") {
                            if (currentUser != null ) {
                                EditExercise(
                                    viewModel = viewModel,
                                    navController = navController,
                                    pickMedia = pickMedia
                                )
                            } else navController.navigate("login")

                        }

                    }
                }
            }
        }

    }


    private fun createAccount(
        email: String,
        password: String,
        confirmPassword: String,
        navController: NavController
    ) {
        // Verifique se a senha e a confirmação da senha são iguais
        if (password != confirmPassword) {
            // Exiba uma mensagem de erro se as senhas não coincidirem
            Toast.makeText(
                baseContext,
                "As senhas não coincidem.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        // Crie a conta do usuário com o email e a senha fornecidos
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sucesso ao criar a conta
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Conta criada com sucesso",
                        Toast.LENGTH_SHORT,
                    ).show()
                    navController.navigate("login")
                } else {
                    // Falha ao criar a conta
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Falha: Email e senha no formato incorreto",
                        Toast.LENGTH_SHORT
                    ).show()
                    // updateUI(null)
                }
            }
    }



    private fun loginAccount(email: String, password: String, navController: NavController) {

        // [START create_user_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    navController.navigate("home")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Falha: Email e/ou senha incorretos",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

}
