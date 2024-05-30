package com.lealapps.teste.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CardOption(imagem: String, desc: String, customColor: Color, route: () -> Unit) {
    Column {
        ElevatedCard(
            modifier = Modifier
                .size(width = 150.dp, height = 100.dp)
                .padding(10.dp)
                .clickable { route() },
            colors = CardDefaults.elevatedCardColors(containerColor = customColor),
        ) {
            AsyncImage(model = imagem, contentDescription = desc)
        }
        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.width(150.dp)) {
            Text(desc)
        }
    }

}