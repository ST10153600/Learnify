package com.learnify.learnify.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login_screen")
    data object Register : Screen("register_screen")
    data object Home : Screen("home_screen")
    data object Quiz : Screen("quiz_screen/{topic}") {
        fun createRoute(topic: String) = "quiz_screen/$topic"
    }
    data object Results : Screen("results_screen/{score}") {
        fun createRoute(score: Int) = "results_screen/$score"
    }
    data object FlaggedQuestions : Screen("flagged_questions_screen")
    /*data object LikedQuestions : Screen("liked_questions_screen")
    data object Profile : Screen("profile_screen")*/
}
