package com.elfennani.readit.compose

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elfennani.readit.compose.login.ExchangeTokenScreen
import com.elfennani.readit.compose.login.LoginScreen

@Composable
fun ReaditApp(code: String?) {
    val navController = rememberNavController()
    val startRoute =
        if (code == null) Screen.LoginScreen.route else Screen.ExchangeToken.createRoute(code)

    NavHost(navController = navController, startDestination = startRoute) {
        composable(
            Screen.LoginScreen.route, Screen.LoginScreen.navArgs
        ) {
            LoginScreen()
        }

        composable(
            Screen.HomePage.route, Screen.HomePage.navArgs
        ) {
            Text(text = "Not implemented yet")
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