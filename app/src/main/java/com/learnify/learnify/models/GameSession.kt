package com.learnify.learnify.models

import com.learnify.learnify.ui.quizScreen.QuizQuestion

data class GameSession(
    val gameId: String = "",
    val player1Id: String = "",
    val player2Id: String = "",
    val questions: List<QuizQuestion> = emptyList(),
    val player1Answers: Map<String, PlayerAnswer> = emptyMap(),
    val player2Answers: Map<String, PlayerAnswer> = emptyMap(),
    val currentQuestionIndex: Int = 0,
    val status: GameStatus = GameStatus.WAITING
)

enum class GameStatus {
    WAITING,
    IN_PROGRESS,
    COMPLETED
}

data class PlayerAnswer(
    val questionId: String = "",
    val selectedOption: String = "",
    val isCorrect: Boolean = false,
    val timestamp: Long = 0L
)


