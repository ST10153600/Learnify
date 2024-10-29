package com.learnify.learnify.ui.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.navigation.Screen
import coil.compose.rememberAsyncImagePainter
import com.learnify.learnify.ui.components.TopBar
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
            Topic("English", generateImageUrl("English"), 20),
            Topic("Programming", generateImageUrl("Programming"), 15),
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Button(
                    onClick = {
                        navController.navigate("matchmaking_screen")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Play Multiplayer")
                }

                // Optional Divider or Spacer for separation
                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(topics) { topic ->
                        TopicCard(topic = topic) {
                            navController.navigate(Screen.LevelSelection.createRoute(topic.name))
                        }
                    }
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
fun TopicCard(topic: Topic, onStartQuizClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable { onStartQuizClick() },
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
                    onClick = onStartQuizClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Start Quiz")
                }
            }
        }
    }
}
