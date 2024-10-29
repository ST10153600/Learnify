package com.learnify.learnify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.learnify.learnify.ui.loginScreen.LoginScreen
import com.learnify.learnify.ui.homeScreen.HomeScreen
import com.learnify.learnify.ui.registerScreen.RegisterScreen

@Composable
fun NavGraphScreen(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
    }
}
