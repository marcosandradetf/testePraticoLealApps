package com.lealapps.teste.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@Composable
fun AppLayout(
    title: String,
    selectedIcon: Int = 0,
    navigateBack: (() -> Unit)? = null,
    navigateToHome: (() -> Unit?)? = null,
    navigateToTraining: (() -> Unit?)? = null,
    navigateToProfile: (() -> Unit?)? = null,
    content: @Composable (Modifier, showSnackBar: (String, String?) -> Unit) -> Unit,
) {
    val selectedItem by remember { mutableIntStateOf(selectedIcon) }
    val items = listOf("Início", "Treinos", "Perfil")

    val selectedIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.FitnessCenter, // ícone de treino
        Icons.Filled.AccountCircle,
    )

    val unselectedIcons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.FitnessCenter, // ícone de treino
        Icons.Outlined.AccountCircle,
    )


    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val showSnackBar: (String, String?) -> Unit = { message, label ->
        scope.launch {
            snackBarHostState.showSnackbar(
                message = message,
                actionLabel = label,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        },
        topBar = {
            Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                TopBar(
                    navigateBack = navigateBack,
                    title = title,
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            if (index in selectedIcons.indices && index in unselectedIcons.indices) {
                                Icon(
                                    imageVector = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                    contentDescription = item,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        label = { Text(text = item, fontSize = 10.sp) },
                        selected = selectedIcon == index,
                        onClick = {
                            handleNavigation(
                                index,
                                navigateToHome,
                                navigateToTraining,
                                navigateToProfile
                            )
                        },
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                content(
                    Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    showSnackBar
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigateBack: (() -> Unit)? = null,
    title: String = "Navigation example",
) {
    TopAppBar(
        modifier = Modifier.height(70.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.secondary,
            actionIconContentColor = MaterialTheme.colorScheme.secondary,
        ),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        },
        navigationIcon = {
            if (navigateBack != null) {
                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                        )
                    }
                }
            }
        },
    )
}

// Função para lidar com navegação ou ações específicas
enum class BottomBar(val value: Int) {
    HOME(0),
    TRAINING(1),
    PROFILE(2),
}
fun handleNavigation(
    index: Int,
    navigateToHome: (() -> Unit?)?,
    navigateToTraining: (() -> Unit?)?,
    navigateToProfile: (() -> Unit?)?
) {
    when (index) {

        BottomBar.HOME.value -> if (navigateToHome != null) {
            navigateToHome()
        }

        BottomBar.TRAINING.value -> if (navigateToTraining != null) {
            navigateToTraining()
        }

        BottomBar.PROFILE.value -> if (navigateToProfile != null) {
            navigateToProfile()
        }

        else -> println("Ação desconhecida")
    }
}