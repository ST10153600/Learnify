package com.learnify.learnify.ui.profileScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.services.FirebaseService
import com.learnify.learnify.ui.components.TopBar

@Composable
fun ProfileScreen(navController: NavController, openDrawer: () -> Unit) {
    val user = FirebaseService.auth.currentUser
    var userData by remember { mutableStateOf<Map<String, Any>?>(null) }

    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            FirebaseService.firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userData = document.data
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Profile",
                onMenuClick = openDrawer
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Username: ${userData?.get("username") ?: "Guest"}")
            Text("Email: ${userData?.get("email") ?: "Not available"}")
            Text("Liked Questions: ${(userData?.get("likedQuestions") as? List<*>)?.size ?: 0}")
            Text("Flagged Questions: ${(userData?.get("flaggedQuestions") as? List<*>)?.size ?: 0}")
            Text("Completed Quizzes: ${(userData?.get("completedQuizzes") as? List<*>)?.size ?: 0}")
        }
    }
}
