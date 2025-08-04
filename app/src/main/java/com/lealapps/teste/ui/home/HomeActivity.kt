package com.lealapps.teste.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.ui.components.AppLayout
import com.lealapps.teste.ui.components.BottomBar


@Composable
fun HomeScreen(navController: NavHostController) {

    val paddingValues = PaddingValues(all = 16.dp)

    // Cor de fundo para dar uma sensação de academia
    val backgroundColor = Color(0xFF1E1E1E)

    AppLayout(
        title = "Início",
        selectedIcon = BottomBar.HOME.value,
        navigateToTraining = {
            navController.navigate(Routes.TRAINING)
        },
        navigateToProfile = {
            navController.navigate(Routes.PROFILE)
        }
    ) { modifier, showSnackBar ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Cabeçalho de boas-vindas
            Text(
                text = "Bem-vindo ao GymHero",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White),
                modifier = Modifier.padding(top = 40.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo de motivação
            Text(
                text = "Seu parceiro de treino, sempre ao seu lado!",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            )

            // Imagem motivacional (pode ser uma imagem de academia ou algo inspirador)
//            Image(
//                painter = painterResource(id = R.drawable.), // Coloque sua imagem motivacional aqui
//                contentDescription = "Imagem de treino",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(250.dp)
//                    .padding(top = 20.dp),
//                contentScale = ContentScale.Crop
//            )

            // Seções rápidas de navegação
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HomeButton(
                    icon = Icons.Filled.FitnessCenter,
                    text = "Treinos",
                    onClick = { navController.navigate(Routes.TRAINING) }
                )

                HomeButton(
                    icon = Icons.Filled.Person,
                    text = "Perfil",
                    onClick = { navController.navigate(Routes.PROFILE) }
                )
            }


        }
    }

}

@Composable
fun HomeButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
            .background(Color(0xFF3A3A3A), shape = CircleShape)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text, color = Color.White)
    }
}
