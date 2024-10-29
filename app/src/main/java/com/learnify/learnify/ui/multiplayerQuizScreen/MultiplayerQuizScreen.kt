package com.learnify.learnify.ui.multiplayerQuizScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.DocumentReference
import com.learnify.learnify.models.GameSession
import com.learnify.learnify.models.PlayerAnswer
import com.learnify.learnify.models.GameStatus
import com.learnify.learnify.services.FirebaseService
import com.learnify.learnify.ui.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun MultiplayerQuizScreen(navController: NavController, gameId: String) {
    val currentUserId = FirebaseService.auth.currentUser?.uid ?: return
    var gameSession by remember { mutableStateOf<GameSession?>(null) }
    val gameRef = FirebaseService.firestore.collection("games").document(gameId)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(gameId) {
        gameRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                gameSession = snapshot.toObject(GameSession::class.java)
            } else {
                navController.popBackStack()
            }
        }
    }

    if (gameSession == null) {
        Scaffold(
            topBar = {
                TopBar(title = "Multiplayer Quiz", onMenuClick = {})
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
        val currentQuestionIndex = gameSession?.currentQuestionIndex ?: 0
        val questions = gameSession?.questions ?: emptyList()

        if (currentQuestionIndex >= questions.size) {
            LaunchedEffect(gameSession?.status) {
                if (gameSession?.status == GameStatus.COMPLETED) {
                    navController.navigate("multiplayer_results_screen/$gameId") {
                        popUpTo("home_screen") { inclusive = false }
                    }
                }
            }
        } else {
            val currentQuestion = questions[currentQuestionIndex]
            Scaffold(
                topBar = {
                    TopBar(title = "Multiplayer Quiz", onMenuClick = {})
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    currentQuestion.options.forEach { option ->
                        Button(
                            onClick = {
                                submitAnswer(
                                    gameId = gameId,
                                    playerId = currentUserId,
                                    questionId = currentQuestion.id,
                                    selectedOption = option,
                                    correctAnswer = currentQuestion.correctAnswer
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(option)
                        }
                    }
                }
            }
        }
    }
}

private fun submitAnswer(
    gameId: String,
    playerId: String,
    questionId: String,
    selectedOption: String,
    correctAnswer: String
) {
    val isCorrect = selectedOption == correctAnswer
    val answer = PlayerAnswer(
        questionId = questionId,
        selectedOption = selectedOption,
        isCorrect = isCorrect
    )

    val gameRef = FirebaseService.firestore.collection("games").document(gameId)
    val answerField = if (playerId == gameRef.id) "player1Answers" else "player2Answers"

    gameRef.update("$answerField.$questionId", answer)
        .addOnSuccessListener {
            checkForNextQuestionOrEndGame(gameRef)
        }
}

private fun checkForNextQuestionOrEndGame(gameRef: DocumentReference) {
    gameRef.get().addOnSuccessListener { document ->
        val gameSession = document.toObject(GameSession::class.java)
        val currentQuestionIndex = gameSession?.currentQuestionIndex ?: 0
        val totalQuestions = gameSession?.questions?.size ?: 0

        val player1Answers = gameSession?.player1Answers?.size ?: 0
        val player2Answers = gameSession?.player2Answers?.size ?: 0

        if (player1Answers > currentQuestionIndex && player2Answers > currentQuestionIndex) {
            if (currentQuestionIndex + 1 >= totalQuestions) {
                gameRef.update("status", GameStatus.COMPLETED)
            } else {
                gameRef.update("currentQuestionIndex", currentQuestionIndex + 1)
            }
        }
    }
}
