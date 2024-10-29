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
                            val selectedOptionLetter = option.substringBefore(")").trim()
                            val currentQuestionAnswerLetter = currentQuestion.correctAnswer.substringBefore(")").trim()
                            if (selectedOptionLetter == currentQuestionAnswerLetter) {
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


fun generateBeginnerQuestions(): List<QuizQuestion> {
    return listOf(
        QuizQuestion(
            id = "B0",
            question = "What is the capital of France?",
            options = listOf("A) Paris", "B) London", "C) Rome", "D) Berlin"),
            correctAnswer = "A) Paris"
        ),
        QuizQuestion(
            id = "B1",
            question = "Which animal is known as the 'King of the Jungle'?",
            options = listOf("A) Lion", "B) Tiger", "C) Bear", "D) Elephant"),
            correctAnswer = "A) Lion"
        ),
        QuizQuestion(
            id = "B2",
            question = "Which planet is known as the Red Planet?",
            options = listOf("A) Mars", "B) Earth", "C) Jupiter", "D) Venus"),
            correctAnswer = "A) Mars"
        ),
        QuizQuestion(
            id = "B3",
            question = "How many colors are there in a rainbow?",
            options = listOf("A) 7", "B) 6", "C) 5", "D) 8"),
            correctAnswer = "A) 7"
        ),
        QuizQuestion(
            id = "B4",
            question = "What is the largest mammal in the world?",
            options = listOf("A) Blue Whale", "B) Elephant", "C) Giraffe", "D) Hippopotamus"),
            correctAnswer = "A) Blue Whale"
        ),
        QuizQuestion(
            id = "B5",
            question = "What is the boiling point of water?",
            options = listOf("A) 100°C", "B) 90°C", "C) 80°C", "D) 70°C"),
            correctAnswer = "A) 100°C"
        ),
        QuizQuestion(
            id = "B6",
            question = "Which gas do plants absorb from the atmosphere?",
            options = listOf("A) Oxygen", "B) Carbon Dioxide", "C) Nitrogen", "D) Hydrogen"),
            correctAnswer = "B) Carbon Dioxide"
        ),
        QuizQuestion(
            id = "B7",
            question = "What is the largest continent?",
            options = listOf("A) Asia", "B) Africa", "C) Europe", "D) North America"),
            correctAnswer = "A) Asia"
        ),
        QuizQuestion(
            id = "B8",
            question = "What is the main ingredient in guacamole?",
            options = listOf("A) Avocado", "B) Tomato", "C) Onion", "D) Pepper"),
            correctAnswer = "A) Avocado"
        ),
        QuizQuestion(
            id = "B9",
            question = "What is the square root of 16?",
            options = listOf("A) 2", "B) 4", "C) 3", "D) 5"),
            correctAnswer = "B) 4"
        )
    )
}

fun generateIntermediateQuestions(): List<QuizQuestion> {
    return listOf(
        QuizQuestion(
            id = "I0",
            question = "Which sentence is grammatically correct?",
            options = listOf("A) She go to the store every day.", "B) She goes to the store every day.", "C) She going to the store every day.", "D) She went to the store every days."),
            correctAnswer = "B"
        ),
        QuizQuestion(
            id = "I1",
            question = "Which is the correct spelling?",
            options = listOf("A) Accomodate", "B) Accommodate", "C) Acommodate", "D) Accomodate"),
            correctAnswer = "B"
        ),
        QuizQuestion(
            id = "I2",
            question = "What is the process of converting light energy into chemical energy called?",
            options = listOf("A) Respiration", "B) Photosynthesis", "C) Digestion", "D) Metabolism"),
            correctAnswer = "B"
        ),
        QuizQuestion(
            id = "I3",
            question = "What is the capital of Japan?",
            options = listOf("A) Tokyo", "B) Beijing", "C) Seoul", "D) Bangkok"),
            correctAnswer = "A"
        ),
        QuizQuestion(
            id = "I4",
            question = "What is the freezing point of water in Fahrenheit?",
            options = listOf("A) 0°F", "B) 32°F", "C) 100°F", "D) 212°F"),
            correctAnswer = "B"
        ),
        QuizQuestion(
            id = "I5",
            question = "Which element has the chemical symbol 'O'?",
            options = listOf("A) Oxygen", "B) Gold", "C) Osmium", "D) Hydrogen"),
            correctAnswer = "A"
        ),
        QuizQuestion(
            id = "I6",
            question = "What is the longest river in the world?",
            options = listOf("A) Amazon River", "B) Nile River", "C) Yangtze River", "D) Mississippi River"),
            correctAnswer = "B"
        ),
        QuizQuestion(
            id = "I7",
            question = "Which planet is known for its rings?",
            options = listOf("A) Jupiter", "B) Mars", "C) Saturn", "D) Neptune"),
            correctAnswer = "C"
        ),
        QuizQuestion(
            id = "I8",
            question = "What is the capital of Canada?",
            options = listOf("A) Toronto", "B) Ottawa", "C) Vancouver", "D) Montreal"),
            correctAnswer = "B"
        ),
        QuizQuestion(
            id = "I9",
            question = "Which of these is a prime number?",
            options = listOf("A) 4", "B) 6", "C) 7", "D) 9"),
            correctAnswer = "C"
        )
    )
}

fun generateHardQuestions(): List<QuizQuestion> {
    return listOf(
        QuizQuestion(
            id = "H0",
            question = "What is the derivative of sin(x)?",
            options = listOf("A) cos(x)", "B) -sin(x)", "C) sin(x)", "D) -cos(x)"),
            correctAnswer = "A"
        ),
        QuizQuestion(
            id = "H1",
            question = "What is the value of π (pi) to two decimal places?",
            options = listOf("A) 3.14", "B) 3.15", "C) 3.13", "D) 3.16"),
            correctAnswer = "A"
        ),
        QuizQuestion(
            id = "H2",
            question = "Which of the following is a non-renewable resource?",
            options = listOf("A) Solar Energy", "B) Wind Energy", "C) Natural Gas", "D) Biomass"),
            correctAnswer = "C"
        ),
        QuizQuestion(
            id = "H3",
            question = "What is the formula for calculating the area of a circle?",
            options = listOf("A) πr^2", "B) 2πr", "C) πd", "D) 4πr^2"),
            correctAnswer = "A"
        ),
        QuizQuestion(
            id = "H4",
            question = "What is the speed of light in vacuum?",
            options = listOf("A) 299,792,458 m/s", "B) 150,000,000 m/s", "C) 300,000,000 m/s", "D) 1,000,000 m/s"),
            correctAnswer = "A"
        ),
        QuizQuestion(
            id = "H5",
            question = "What is the main component of natural gas?",
            options = listOf("A) Ethane", "B) Methane", "C) Propane", "D) Butane"),
            correctAnswer = "B"
        ),
        QuizQuestion(
            id = "H6",
            question = "What is the primary gas found in the Earth's atmosphere?",
            options = listOf("A) Oxygen", "B) Carbon Dioxide", "C) Nitrogen", "D) Hydrogen"),
            correctAnswer = "C"
        ),
        QuizQuestion(
            id = "H7",
            question = "What is the principle of superposition in physics?",
            options = listOf("A) Force is equal to mass times acceleration", "B) The total response caused by multiple stimuli is the sum of responses that would have been caused by each stimulus individually", "C) Energy cannot be created or destroyed", "D) For every action, there is an equal and opposite reaction"),
            correctAnswer = "B"
        ),
        QuizQuestion(
            id = "H8",
            question = "What is the hardest natural substance on Earth?",
            options = listOf("A) Gold", "B) Iron", "C) Diamond", "D) Quartz"),
            correctAnswer = "C"
        ),
        QuizQuestion(
            id = "H9",
            question = "What is the chemical formula for table salt?",
            options = listOf("A) NaCl", "B) KCl", "C) NaOH", "D) H2O"),
            correctAnswer = "A"
        )
    )
}
