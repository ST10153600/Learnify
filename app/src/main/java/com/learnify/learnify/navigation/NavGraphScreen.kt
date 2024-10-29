package com.learnify.learnify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.learnify.learnify.ui.flaggedQuestionsScreen.FlaggedQuestionsScreen
import com.learnify.learnify.ui.loginScreen.LoginScreen
import com.learnify.learnify.ui.homeScreen.HomeScreen
import com.learnify.learnify.ui.levelSelectionScreen.LevelSelectionScreen
import com.learnify.learnify.ui.likedQuestionsScreen.LikedQuestionsScreen
import com.learnify.learnify.ui.profileScreen.ProfileScreen
import com.learnify.learnify.ui.quizScreen.QuizScreen
import com.learnify.learnify.ui.registerScreen.RegisterScreen
import com.learnify.learnify.ui.resultsScreen.ResultsScreen

@Composable
fun NavGraphScreen(
    navController: NavHostController,
    openDrawer: () -> Unit,
    isLoggedIn: Boolean,
    onLogout: () -> Unit
) {
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, openDrawer = openDrawer)
        }
        composable(
            route = Screen.LevelSelection.route,
            arguments = listOf(navArgument("topic") {})
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic") ?: "General"
            LevelSelectionScreen(navController = navController, topic = topic)
        }
        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument("topic") {},
                navArgument("level") {}
            )
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic") ?: "General"
            val level = backStackEntry.arguments?.getString("level") ?: "Beginner"
            QuizScreen(navController = navController, topic = topic, level = level)
        }
        composable(
            route = Screen.Results.route,
            arguments = listOf(navArgument("score") { defaultValue = 0 })
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            ResultsScreen(navController = navController, score = score)
        }
        composable(route = Screen.FlaggedQuestions.route) {
            FlaggedQuestionsScreen(navController = navController, openDrawer = openDrawer)
        }
        composable(route = Screen.LikedQuestions.route) {
            LikedQuestionsScreen(navController = navController, openDrawer = openDrawer)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, openDrawer = openDrawer)
        }
    }
}
