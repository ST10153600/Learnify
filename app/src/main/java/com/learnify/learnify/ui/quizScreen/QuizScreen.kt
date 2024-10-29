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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    var isFlagged: Boolean = false,
    var isFavorited: Boolean = false
)

@Composable
fun QuizScreen(navController: NavController, topic: String, level: String) {
    val questions = remember { loadQuestionsForLevel(level) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(10) }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isQuizOver by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableStateOf(0) }

    val currentQuestion = questions[currentQuestionIndex]

    LaunchedEffect(currentQuestionIndex) {
        timerJob?.cancel()
        timeLeft = 10
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
                isFavorited = currentQuestion.isFavorited,
                onFlagClick = {
                    currentQuestion.isFlagged = !currentQuestion.isFlagged
                },
                onFavoriteClick = {
                    currentQuestion.isFavorited = !currentQuestion.isFavorited
                }
            )
        }
    }
}

@Composable
fun QuizBottomBar(
    timeLeft: Int,
    isFlagged: Boolean,
    isFavorited: Boolean,
    onFlagClick: () -> Unit,
    onFavoriteClick: () -> Unit
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
        IconButton(onClick = { onFavoriteClick() }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite Question",
                tint = if (isFavorited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun loadQuestionsForLevel(level: String): List<QuizQuestion> {
    return when (level) {
        "Beginner" -> listOf(
            QuizQuestion("Which sentence is grammatically correct?", listOf("A) She go to the store every day.", "B) She goes to the store every day.", "C) She going to the store every day.", "D) She went to the store every days."), "B"),
            QuizQuestion("Which is the correct spelling?", listOf("A) Accomodate", "B) Accommodate", "C) Acommodate", "D) Accomodete"), "B"),
            // Add more beginner-level questions
        )
        "Intermediate" -> listOf(
            QuizQuestion("Choose the sentence with correct punctuation.", listOf("A) \"It's raining, I forgot my umbrella.\"", "B) \"Its raining; I forgot my umbrella.\"", "C) \"It’s raining. I forgot my umbrella.\"", "D) \"Its raining I forgot my umbrella.\""), "C"),
            QuizQuestion("Select the synonym of \"elated\":", listOf("A) Excited", "B) Sad", "C) Angry", "D) Bored"), "A"),
            // Add more intermediate-level questions
        )
        "Advanced" -> listOf(
            QuizQuestion("Which sentence uses the correct homophone?", listOf("A) Their going to the movies later.", "B) There going to the movies later.", "C) They’re going to the movies later.", "D) Thier going to the movies later."), "C"),
            QuizQuestion("Identify the sentence that uses \"affect\" correctly:", listOf("A) The movie had an affect on me.", "B) The weather affects my mood.", "C) The medication had a positive affect.", "D) She was deeply affect."), "B"),
            // Add more advanced-level questions
        )
        else -> emptyList()
    }
}
