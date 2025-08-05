package com.lealapps.teste.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.viewmodel.UserViewModel

@Composable
fun SignUp(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(userViewModel.isAuthenticated) {
        if (userViewModel.isAuthenticated) {
            navController.navigate(Routes.HOME)
        }
    }

    if (userViewModel.message.isNotBlank()) {
        Toast.makeText(context, userViewModel.message, Toast.LENGTH_LONG).show()
        userViewModel.message = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    // Fechar o teclado ao tocar em qualquer lugar da tela
                    keyboardController?.hide()
                }
            }
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
        ) {

            // Caixa do login
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
            ) {
                // Título
                Text(
                    text = "GymHero™",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Descrição
                Text(
                    text = "Olá, seja bem-vindo!!\n\nPreencha os dados abaixo para criar sua conta.",
                    modifier = Modifier.padding(bottom = 24.dp),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    value = userViewModel.name,
                    onValueChange = { userViewModel.name = it },
                    label = {
                        Text(
                            text = "Seu Nome",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo de Email
                OutlinedTextField(
                    value = userViewModel.email,
                    onValueChange = { userViewModel.email = it },
                    label = { Text(text = "Email", color = MaterialTheme.colorScheme.onSurface) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo de Senha
                OutlinedTextField(
                    value = userViewModel.password,
                    onValueChange = { userViewModel.password = it },
                    label = { Text(text = "Senha", color = MaterialTheme.colorScheme.onSurface) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                var confirmPasswordVisible by remember { mutableStateOf(false) }

                // Campo de Confirmação de Senha
                OutlinedTextField(
                    value = userViewModel.confirmPassword,
                    onValueChange = { userViewModel.confirmPassword = it },
                    label = {
                        Text(
                            text = "Confirmação de senha",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff

                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (confirmPasswordVisible) "Ocultar senha" else "Mostrar senha"
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (userViewModel.email.isNotEmpty() && userViewModel.password.isNotEmpty() && userViewModel.confirmPassword.isNotEmpty()) {
                    ElevatedButton(
                        onClick = { userViewModel.createAccount() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Criar conta",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                }
                TextButton(
                    onClick = { navController.navigate(Routes.LOGIN) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Já possuí cadastro?",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }


            }
        }
    }
}

@Preview
@Composable
fun PrevSignUp() {
    SignUp(
        navController = rememberNavController(),
        userViewModel = UserViewModel()
    )
}