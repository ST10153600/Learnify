package com.learnify.learnify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.learnify.learnify.navigation.NavGraphScreen
import com.learnify.learnify.navigation.Screen
import com.learnify.learnify.ui.components.DrawerContent
import com.learnify.learnify.ui.theme.LearnifyTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnifyTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerContent = {
                        DrawerContent(
                            navController = navController,
                            onDestinationClicked = { route ->
                                coroutineScope.launch { drawerState.close() }
                                navController.navigate(route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        )
                    },
                    drawerState = drawerState
                ) {
                    NavGraphScreen(
                        navController = navController,
                        openDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }
            }
        }
    }
}
