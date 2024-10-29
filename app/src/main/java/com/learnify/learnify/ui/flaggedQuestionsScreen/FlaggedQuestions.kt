package com.learnify.learnify.ui.flaggedQuestionsScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.learnify.learnify.ui.components.TopBar

@Composable
fun FlaggedQuestionsScreen(
    navController: NavController,
    openDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Flagged Questions",
                onMenuClick = openDrawer
            )
        }
    ) { innerPadding ->
        Text(
            text = "Flagged Questions",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
