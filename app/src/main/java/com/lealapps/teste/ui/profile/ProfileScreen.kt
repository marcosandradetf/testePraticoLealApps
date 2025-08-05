package com.lealapps.teste.ui.profile

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lealapps.teste.navigation.Routes
import com.lealapps.teste.ui.components.AppLayout
import com.lealapps.teste.ui.components.BottomBar
import com.lealapps.teste.viewmodel.UserViewModel


@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current

    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val currentVersionName = packageInfo.versionName
    val currentVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        @Suppress("DEPRECATION")
        packageInfo.versionCode.toLong()
    }


    var modalPassword by remember { mutableStateOf(false) }
    val currentUser = userViewModel.user

    AppLayout(
        title = "Perfil",
        selectedIcon = BottomBar.PROFILE.value,
        navigateToHome = { navController.navigate(Routes.HOME) },
        navigateToTraining = { navController.navigate(Routes.TRAINING) }
    ) { modifier, showSnackBar ->

        if(userViewModel.passwordUpdated) {
            modalPassword = false
            showSnackBar("Senha alterada com sucesso", null)
            userViewModel.passwordUpdated = false
            userViewModel.message = ""
        }

        if (modalPassword) {
            ChangePasswordModal(
                show = true,
                onDismiss = { modalPassword = false },
                onChangePassword = { oldPass, newPass, confirmPass ->
                    // Chame a função do ViewModel para alterar senha, ex:
                    userViewModel.changePassword(oldPass, newPass, confirmPass)
                },
                isLoading = userViewModel.isLoading,
                errorMessage = userViewModel.message
            )
        }


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Card com info do usuário
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informações da Conta",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(12.dp))

                    ProfileInfoRow(label = "Nome", value = currentUser?.displayName ?: "Não disponível")
                    ProfileInfoRow(label = "Email", value = currentUser?.email ?: "Não disponível")
                    ProfileInfoRow(label = "UID", value = currentUser?.uid ?: "Não disponível")
                }
            }

            // Card com segurança e senha
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Segurança",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { modalPassword = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B90FE))
                    ) {
                        Icon(Icons.Filled.Lock, contentDescription = "Alterar senha")
                        Spacer(Modifier.width(8.dp))
                        Text("Alterar Senha")
                    }
                }
            }

            // Versão do app
            ListItem(
                headlineContent = {
                    Text("Versão: $currentVersionName ($currentVersionCode)")
                },
                leadingContent = {
                    Icon(Icons.Filled.Info, contentDescription = "Versão")
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                shadowElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            )

            // Logout
            ListItem(
                headlineContent = {
                    Text("Sair da conta", color = Color.Red)
                },
                leadingContent = {
                    Icon(Icons.Filled.ExitToApp, contentDescription = "Sair", tint = Color.Red)
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        userViewModel.signOut()
                    }
            )
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(
            text = value,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EditProfileDialog(
    title: String,
    initialValue: String = "",
    label: String,
    confirmText: String = "Confirmar",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember { mutableStateOf(initialValue) }
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text(label) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    onConfirm(value)
                }
            ) {
                Text(confirmText.uppercase())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar".uppercase())
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ChangePasswordModal(
    show: Boolean,
    onDismiss: () -> Unit,
    onChangePassword: (oldPassword: String, newPassword: String, confirmPassword: String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    if (show) {
        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var showOldPassword by remember { mutableStateOf(false) }
        var showNewPassword by remember { mutableStateOf(false) }
        var showConfirmPassword by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Alterar Senha", style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Senha atual") },
                        visualTransformation = if (showOldPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (showOldPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { showOldPassword = !showOldPassword }) {
                                Icon(imageVector = image, contentDescription = "Toggle senha atual")
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nova senha") },
                        visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (showNewPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                Icon(imageVector = image, contentDescription = "Toggle nova senha")
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar nova senha") },
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(imageVector = image, contentDescription = "Toggle confirmar senha")
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!errorMessage.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onChangePassword(oldPassword, newPassword, confirmPassword) },
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Alterar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}
