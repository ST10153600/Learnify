package com.learnify.learnify.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learnify.learnify.navigation.Screen

@Composable
fun DrawerContent(
    navController: NavController,
    onDestinationClicked: (String) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Learnify",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Divider()
        NavigationDrawerItem(
            label = { Text("Home") },
            selected = false,
            onClick = { onDestinationClicked(Screen.Home.route) }
        )
        NavigationDrawerItem(
            label = { Text("Flagged Questions") },
            selected = false,
            onClick = { onDestinationClicked(Screen.FlaggedQuestions.route) }
        )
        NavigationDrawerItem(
            label = { Text("Liked Questions") },
            selected = false,
            onClick = { onDestinationClicked(Screen.LikedQuestions.route) }
        )
        NavigationDrawerItem(
            label = { Text("Profile") },
            selected = false,
            onClick = { onDestinationClicked(Screen.Profile.route) }
        )
        NavigationDrawerItem(
            label = { Text("Logout") },
            selected = false,
            onClick = {
                onLogout()
                onDestinationClicked(Screen.Login.route)
            }
        )
    }
}
