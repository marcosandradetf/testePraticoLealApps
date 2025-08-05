package com.lealapps.teste.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector
) {
    val colors = MaterialTheme.colorScheme

    AlertDialog(
        modifier = Modifier.border(
            BorderStroke(
                1.dp,
                colors.outline, // cor do contorno do tema
            ),
            shape = RoundedCornerShape(25.dp)
        ),
        containerColor = colors.surfaceVariant, // cor do fundo do diálogo
        iconContentColor = colors.error,       // cor do ícone (erro = vermelho)
        title = {
            Text(
                text = dialogTitle,
                color = colors.onSurfaceVariant // cor do texto do título
            )
        },
        text = {
            Text(
                text = dialogText,
                color = colors.onSurfaceVariant // cor do texto do corpo
            )
        },
        onDismissRequest = { onDismissRequest() },

        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text(
                    text = "Confirmar",
                    color = colors.error // botão confirmação em vermelho
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(
                    "Cancelar",
                    color = colors.primary // botão cancelar com cor principal
                )
            }
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.error
            )
        }
    )
}
