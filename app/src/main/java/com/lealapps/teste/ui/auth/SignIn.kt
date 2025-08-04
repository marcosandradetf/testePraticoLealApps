package com.lealapps.teste.ui.auth

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    viewModel: UserViewModel,
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel.isAuthenticated) {
        if(viewModel.isAuthenticated) {
            navController.navigate(Routes.HOME)
        }
    }

    // Layout principal
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Caixa do login
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .width(380.dp)
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
                text = "Insira suas credenciais para acessar o GymHero",
                modifier = Modifier.padding(bottom = 24.dp),
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Campo de Email
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
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
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Senha
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text(text = "Senha", color = MaterialTheme.colorScheme.onSurface) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botão de Login
            ElevatedButton(
                onClick = { viewModel.signIn() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botão de Criar Conta
            TextButton(
                onClick = { navController.navigate(Routes.SIGN_UP) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Criar Conta",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Preview
@Composable
fun PrevSignIn() {
    Login(
        viewModel = UserViewModel(),
        navController = rememberNavController()
    )
}
