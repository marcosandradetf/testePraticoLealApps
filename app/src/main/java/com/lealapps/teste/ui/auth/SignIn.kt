package com.lealapps.teste.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lealapps.teste.ui.auth.ui.theme.TesteTheme


@Composable
fun Login() {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    fun sendCred(u: String, p: String) {
        println(u)
        println(p)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFE8E1CE)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(400.dp)
        ) {
            AsyncImage(
                model = "https://img.freepik.com/vetores-gratis/fundo-com-emblema-retro-do-crossfit_23-2147620111.jpg?w=826&t=st=1716669813~exp=1716670413~hmac=fa0f5321e1bfd22b1ab09634c3c8563bcbf6d3e78cd1a8d3202829b5aee958e4",
                contentDescription = "Logo do app",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .width(400.dp))
            OutlinedTextField(
                textStyle = TextStyle(Color(0xFF613F23)),
                value = user,
                onValueChange = { user = it },
                label = { Text(text = "Usuario:", color = Color(0xFF613F23)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                textStyle = TextStyle(Color(0xFF613F23)),
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password:", color = Color(0xFF613F23)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ElevatedButton(
            onClick = { sendCred(user, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .width(400.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text(
                text = "Login",
                color = Color.White)
        }
    }
}
