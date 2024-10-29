package com.learnify.learnify.ui.likedQuestionsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.services.FirebaseService
import com.learnify.learnify.ui.components.TopBar

@Composable
fun LikedQuestionsScreen(navController: NavController, openDrawer: () -> Unit) {
    val user = FirebaseService.auth.currentUser
    var likedQuestions by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            FirebaseService.firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        likedQuestions = document.get("likedQuestions") as? List<String> ?: emptyList()
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Liked Questions",
                onMenuClick = openDrawer
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (likedQuestions.isEmpty()) {
                Text("You haven't liked any questions yet.")
            } else {
                likedQuestions.forEach { questionId ->
                    Text("Question ID: $questionId")
                }
            }
        }
    }
}
