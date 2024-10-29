package com.learnify.learnify.ui.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.navigation.Screen
import com.learnify.learnify.ui.components.TopBar
import coil.compose.rememberAsyncImagePainter
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class Topic(
    val name: String,
    val imageUrl: String,
    val questionCount: Int
)

@Composable
fun HomeScreen(
    navController: NavController,
    openDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Home",
                onMenuClick = openDrawer
            )
        }
    ) { innerPadding ->
        val topics = listOf(
            Topic("Literature", generateImageUrl("Literature"), 20),
            Topic("Grammar", generateImageUrl("Grammar"), 15),
            Topic("Spelling", generateImageUrl("Spelling"), 10),
            Topic("General Knowledge", generateImageUrl("General Knowledge"), 25),
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(topics) { topic ->
                    TopicCard(topic = topic, navController = navController)
                }
            }
        }
    }
}

fun generateImageUrl(topicName: String): String {
    val encodedTopic = URLEncoder.encode(topicName, StandardCharsets.UTF_8.toString())
    return "https://placehold.co/600x200?text=$encodedTopic&font=roboto"
}

@Composable
fun TopicCard(topic: Topic, navController: NavController) {
    var showLevelDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable { showLevelDialog = true },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = topic.imageUrl),
                contentDescription = "${topic.name} Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = topic.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${topic.questionCount} Questions",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = { showLevelDialog = true },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Start Quiz")
                }
            }
        }
    }

    if (showLevelDialog) {
        LevelSelectionDialog(
            onDismiss = { showLevelDialog = false },
            onLevelSelected = { selectedLevel ->
                navController.navigate(Screen.Quiz.createRoute(topic.name, selectedLevel))
                showLevelDialog = false
            }
        )
    }
}

@Composable
fun LevelSelectionDialog(onDismiss: () -> Unit, onLevelSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Level") },
        text = {
            Column {
                Button(onClick = { onLevelSelected("Beginner") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Beginner")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onLevelSelected("Intermediate") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Intermediate")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onLevelSelected("Advanced") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Advanced")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
