package com.learnify.learnify.ui.multiplayerResultsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.models.GameSession
import com.learnify.learnify.services.FirebaseService
import com.learnify.learnify.ui.components.TopBar

@Composable
fun MultiplayerResultsScreen(navController: NavController, gameId: String) {
    var gameSession by remember { mutableStateOf<GameSession?>(null) }
    val gameRef = FirebaseService.firestore.collection("games").document(gameId)

    LaunchedEffect(gameId) {
        gameRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                gameSession = document.toObject(GameSession::class.java)
            } else {
                navController.popBackStack()
            }
        }
    }

    if (gameSession == null) {
        Scaffold(
            topBar = {
                TopBar(title = "Game Results", onMenuClick = {})
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    } else {
        val player1Score = gameSession?.player1Answers?.values?.count { it.isCorrect } ?: 0
        val player2Score = gameSession?.player2Answers?.values?.count { it.isCorrect } ?: 0

        val currentUserId = FirebaseService.auth.currentUser?.uid

        val isWinner = when {
            player1Score == player2Score -> null
            currentUserId == gameSession?.player1Id && player1Score > player2Score -> true
            currentUserId == gameSession?.player2Id && player2Score > player1Score -> true
            else -> false
        }

        Scaffold(
            topBar = {
                TopBar(title = "Game Results", onMenuClick = {})
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (isWinner) {
                        null -> "It's a Tie!"
                        true -> "You Win!"
                        false -> "You Lose!"
                    },
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your Score: ${if (currentUserId == gameSession?.player1Id) player1Score else player2Score}")
                Text("Opponent's Score: ${if (currentUserId == gameSession?.player1Id) player2Score else player1Score}")
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { navController.navigate("home_screen") }) {
                    Text("Back to Home")
                }
            }
        }
    }
}
