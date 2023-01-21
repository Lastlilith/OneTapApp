package com.imnidasoftware.onetapapp.navigation

sealed class Screen(val route: String) {
    object Login: Screen(route = "login_screen")
    object Profile: Screen(route = "profile_screen")
}
