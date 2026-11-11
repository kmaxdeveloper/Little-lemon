package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyNavigation(context: Context) {
    val navController = rememberNavController()
    val sharedPreferences = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
    val startDestination = if (isLoggedIn) "Home" else "Onboarding"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("Onboarding") {
            OnboardingScreen(navController = navController, context = context)
        }
        composable("Home") {
            HomeScreen(navController = navController, context = context)
        }
        composable("Profile") {
            ProfileScreen(navController = navController, context = context)
        }
    }
}
