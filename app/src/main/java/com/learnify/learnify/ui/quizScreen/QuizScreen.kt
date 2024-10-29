package com.learnify.learnify.ui.quizScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.navigation.Screen
import com.learnify.learnify.services.FirebaseService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class QuizQuestion(
    val id: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: String = "",
    var isFlagged: Boolean = false,
    var isLiked: Boolean = false
)

@Composable
fun QuizScreen(navController: NavController, topic: String, level: String) {
    val questions = remember { generateTestQuestions(level) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(getTimePerQuestion(level)) }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isQuizOver by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableStateOf(0) }

    val currentQuestion = questions[currentQuestionIndex]

    LaunchedEffect(currentQuestionIndex) {
        timerJob?.cancel()
        timeLeft = getTimePerQuestion(level)
        timerJob = coroutineScope.launch {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
            } else {
                isQuizOver = true
            }
        }
    }

    if (isQuizOver) {
        FirebaseService.addCompletedQuiz(topic)
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Results.createRoute(correctAnswers)) {
                popUpTo(Screen.Home.route) { inclusive = false }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = currentQuestion.question,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.onBackground
            )

            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentQuestion.options.forEach { option ->
                    Button(
                        onClick = {
                            timerJob?.cancel()
                            if (option == currentQuestion.correctAnswer) {
                                correctAnswers++
                            }
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                isQuizOver = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = option)
                    }
                }
            }

            QuizBottomBar(
                timeLeft = timeLeft,
                isFlagged = currentQuestion.isFlagged,
                isLiked = currentQuestion.isLiked,
                onFlagClick = {
                    currentQuestion.isFlagged = !currentQuestion.isFlagged
                    if (currentQuestion.isFlagged) {
                        FirebaseService.addFlaggedQuestion(currentQuestion.id)
                    } else {
                        FirebaseService.removeFlaggedQuestion(currentQuestion.id)
                    }
                },
                onLikeClick = {
                    currentQuestion.isLiked = !currentQuestion.isLiked
                    if (currentQuestion.isLiked) {
                        FirebaseService.addLikedQuestion(currentQuestion.id)
                    } else {
                        FirebaseService.removeLikedQuestion(currentQuestion.id)
                    }
                }
            )
        }
    }
}

@Composable
fun QuizBottomBar(
    timeLeft: Int,
    isFlagged: Boolean,
    isLiked: Boolean,
    onFlagClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    BottomAppBar(
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        IconButton(onClick = { onFlagClick() }) {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = "Flag Question",
                tint = if (isFlagged) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Time: $timeLeft",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { onLikeClick() }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Like Question",
                tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun getTimePerQuestion(level: String): Int {
    return when (level) {
        "Beginner" -> 20
        "Intermediate" -> 15
        "Hard" -> 6
        else -> 20
    }
}

fun generateTestQuestions(level: String): List<QuizQuestion> {
    return when (level) {
        "Beginner" -> generateBeginnerQuestions()
        "Intermediate" -> generateIntermediateQuestions()
        "Hard" -> generateHardQuestions()
        else -> generateBeginnerQuestions()
    }
}

// TODO: YOU CAN ADD YOUR DIFFERENT QUESTIONS HERE!!! USE QuizQuestions() as one component per question ///

fun generateBeginnerQuestions(): List<QuizQuestion> {
    return List(10) { index ->
        QuizQuestion(
            id = "B$index",
            question = "Beginner Question ${index + 1}",
            options = listOf("Option A", "Option B", "Option C", "Option D"),
            correctAnswer = "Option A"
        )
    }
}

fun generateIntermediateQuestions(): List<QuizQuestion> {
    return List(10) { index ->
        QuizQuestion(
            id = "I$index",
            question = "Intermediate Question ${index + 1}",
            options = listOf("Option A", "Option B", "Option C", "Option D"),
            correctAnswer = "Option B"
        )
    }
}

fun generateHardQuestions(): List<QuizQuestion> {
    return List(10) { index ->
        QuizQuestion(
            id = "H$index",
            question = "Hard Question ${index + 1}",
            options = listOf("Option A", "Option B", "Option C", "Option D"),
            correctAnswer = "Option C"
        )
    }
}
