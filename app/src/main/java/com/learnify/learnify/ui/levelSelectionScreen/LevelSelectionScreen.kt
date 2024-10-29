package com.learnify.learnify.ui.levelSelectionScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionScreen(navController: NavController, topic: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Level") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LevelCard(level = "Beginner", description = "For starters. 20 seconds per question.") {
                navController.navigate(Screen.Quiz.createRoute(topic, "Beginner"))
            }
            LevelCard(level = "Intermediate", description = "A bit challenging. 15 seconds per question.") {
                navController.navigate(Screen.Quiz.createRoute(topic, "Intermediate"))
            }
            LevelCard(level = "Hard", description = "For experts. 6 seconds per question.") {
                navController.navigate(Screen.Quiz.createRoute(topic, "Hard"))
            }
        }
    }
}

@Composable
fun LevelCard(level: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = level,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
