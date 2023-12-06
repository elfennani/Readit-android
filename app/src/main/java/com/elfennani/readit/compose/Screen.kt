package com.elfennani.readit.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArgs: List<NamedNavArgument> = emptyList()
) {
    data object LoginScreen : Screen(route = "login")

    data object HomePage : Screen(
        route = "home/{userId}",
        navArgs = listOf(navArgument("userId") {
            type = NavType.StringType
        })
    )

    data object ExchangeToken : Screen(
        route = "exchange/{code}",
        navArgs = listOf(
            navArgument("code") {
                type = NavType.StringType
            }
        )
    ){
        fun createRoute(code:String) = "exchange/$code"
    }
}