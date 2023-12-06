package com.elfennani.readit.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elfennani.readit.compose.home.HomeFeed
import com.elfennani.readit.compose.login.ExchangeTokenScreen
import com.elfennani.readit.compose.login.LoginScreen

@Composable
fun ReaditApp(code: String?, user: String?) {
    val navController = rememberNavController()
    val startRoute = when {
        code != null -> Screen.ExchangeToken.route
        user != null -> Screen.HomePage.route
        else -> Screen.LoginScreen.route
    }

    NavHost(navController = navController, startDestination = startRoute) {
        composable(
            Screen.LoginScreen.route, Screen.LoginScreen.navArgs
        ) {
            LoginScreen()
        }

        composable(
            Screen.HomePage.route,
            arguments = Screen.HomePage.createArgs(user)
        ) {
            val userId = checkNotNull(it.arguments?.getString("userId"))
            HomeFeed(navController = navController, userId = userId)
        }

        composable(
            Screen.ExchangeToken.route, Screen.ExchangeToken.navArgs
        ) {
            ExchangeTokenScreen(
                code = code ?: ""
            )
        }

    }
}