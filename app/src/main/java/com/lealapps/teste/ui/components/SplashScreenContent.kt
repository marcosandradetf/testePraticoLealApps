package com.lealapps.teste.ui.components

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SplashScreenContent() {
    Surface(
        color = Color.Black
    ) {
        Text(text = "Welcome")
    }
}