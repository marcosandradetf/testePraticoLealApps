package com.lealapps.teste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lealapps.teste.navigation.AppNavigation
import com.lealapps.teste.ui.theme.TesteTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()

        setContent {

            TesteTheme {
                AppNavigation()
            }
        }

    }

}
