package com.lealapps.teste.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SignUp(
    sendCred: (String, String, String) -> Unit,
) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFE8E1CE)),
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
                label = { Text(text = "Email:", color = Color(0xFF613F23)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                textStyle = TextStyle(Color(0xFF613F23)),
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Senha:", color = Color(0xFF613F23)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                textStyle = TextStyle(Color(0xFF613F23)),
                value = passwordConfirm,
                onValueChange = { passwordConfirm = it },
                label = { Text(text = "Confirme a senha:", color = Color(0xFF613F23)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (user.length > 0 && password.length > 0 && passwordConfirm.length > 0){
            ElevatedButton(
                onClick = { sendCred(user, password, passwordConfirm) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .width(400.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                Text(
                    text = "Criar conta",
                    color = Color.White)
            }
        }


    }
}