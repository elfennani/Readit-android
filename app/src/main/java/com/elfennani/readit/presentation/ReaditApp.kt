package com.elfennani.readit.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elfennani.readit.presentation.auth.ExchangeTokenScreen
import com.elfennani.readit.presentation.homefeed.HomeFeed
import com.elfennani.readit.presentation.login.LoginScreen

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
            Screen.ExchangeToken.route, Screen.ExchangeToken.createArgs(code)
        ) {
            ExchangeTokenScreen(
                code = code ?: ""
            )
        }

    }
}