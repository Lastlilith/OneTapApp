package com.imnidasoftware.onetapapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.imnidasoftware.onetapapp.presentation.screen.login.LoginScreen
import com.imnidasoftware.onetapapp.presentation.screen.profile.ProfileScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}