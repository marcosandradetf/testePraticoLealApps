package com.lealapps.teste.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstCreate(
    navController: NavHostController,
    route: String,
    label: String
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(5.dp)
            .width(250.dp)
            .border(
                BorderStroke(
                    1.dp,
                    Color(0xFF54575C)
                ),
                shape = RoundedCornerShape(10.dp)
            ),
        onClick = { /*TODO*/ }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .clickable { navController.navigate(route) }
        ) {
            Icon(
                    modifier = Modifier.border(
                        BorderStroke(
                            1.dp,
                            Color(0xFF54575C)
                        ),
                        shape = RoundedCornerShape(50.dp),
                    )
                        .padding(10.dp)
                        .size(50.dp),
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = label,
                    tint = Color(0xFFA0D286)
                )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = label)
            }
        }
    }
}