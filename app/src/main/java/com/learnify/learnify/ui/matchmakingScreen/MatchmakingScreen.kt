package com.learnify.learnify.ui.matchmakingScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.services.FirebaseService
import com.learnify.learnify.ui.components.TopBar

@Composable
fun MatchmakingScreen(navController: NavController) {
    var isWaiting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        FirebaseService.joinMatchmakingQueue(
            onMatchFound = { gameId ->
                navController.navigate("multiplayer_quiz_screen/$gameId")
            },
            onWaiting = {
                isWaiting = true
            },
            onError = { e ->
                errorMessage = e.message
                println(e.message)
            }
        )
    }

    Scaffold(
        topBar = {
            TopBar(title = "Matchmaking", onMenuClick = {})
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isWaiting) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Searching for an opponent...")
                }
            }
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
