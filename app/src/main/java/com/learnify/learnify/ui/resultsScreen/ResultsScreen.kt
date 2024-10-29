package com.learnify.learnify.ui.resultsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.navigation.Screen
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit
import android.util.Log

@Composable
fun ResultsScreen(navController: NavController, score: Int, total: Int) {
    Log.d("ResultsScreen", "Received score: $score out of $total")

    val highScoreThreshold = 80
    val averageScoreThreshold = 50

    val highScoreMessage = "Excellent work! You're a quiz master!"
    val averageScoreMessage = "Good effort! Keep practicing to improve."
    val lowScoreMessage = "Don't give up! Try again to boost your score."
    val allCongratsMessage = "Congratulations to all participants!"

    val scorePercentage = if (total > 0) (score.toDouble() / total) * 100 else 0.0
    val resultMessage = when {
        scorePercentage >= highScoreThreshold -> highScoreMessage
        scorePercentage >= averageScoreThreshold -> averageScoreMessage
        else -> lowScoreMessage
    }

    val confettiColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    ).map { it.toArgb() }

    val party = remember {
        Party(
            angle = Angle.BOTTOM,
            spread = 360,
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            colors = confettiColors,
            size = listOf(Size.SMALL, Size.MEDIUM),
            timeToLive = 2000L,
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(300),
            position = Position.Relative(0.5, 0.0)
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Display confetti
            KonfettiView(
                parties = listOf(party),
                modifier = Modifier.fillMaxSize()
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = resultMessage,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Score: $score/$total",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Score Percentage: ${"%.2f".format(scorePercentage)}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Back to Home")
                }
            }
        }
    }
}
