package com.learnify.learnify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.learnify.learnify.navigation.NavGraphScreen
import com.learnify.learnify.navigation.Screen
import com.learnify.learnify.services.FirebaseService
import com.learnify.learnify.ui.components.DrawerContent
import com.learnify.learnify.ui.theme.LearnifyTheme
import com.learnify.learnify.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnifyTheme {
                val authViewModel: AuthViewModel = viewModel()
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()
                val currentUser by authViewModel.currentUser.collectAsState()
                val isLoggedIn = currentUser != null

                val onLogout = {
                    FirebaseService.auth.signOut()
                    coroutineScope.launch { drawerState.close() }
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }

                ModalNavigationDrawer(
                    drawerContent = {
                        DrawerContent(
                            navController = navController,
                            onDestinationClicked = { route ->
                                coroutineScope.launch { drawerState.close() }
                                navController.navigate(route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            },
                            onLogout = onLogout
                        )
                    },
                    drawerState = drawerState
                ) {
                    NavGraphScreen(
                        navController = navController,
                        openDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        isLoggedIn = isLoggedIn,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}
