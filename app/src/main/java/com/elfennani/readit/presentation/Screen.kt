package com.elfennani.readit.presentation

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
    ){
        fun createRoute(userId:String) = "home/$userId"
        fun createArgs(userId: String?) = listOf(navArgument("userId"){
            type = NavType.StringType
            if(userId != null){
                defaultValue = userId
            }
        })
    }

    data object ExchangeToken : Screen(
        route = "exchange/{code}",
        navArgs = listOf(
            navArgument("code") {
                type = NavType.StringType
            }
        )
    ){
        fun createRoute(code:String) = "exchange/$code"

        fun createArgs(code: String?) = listOf(navArgument("code"){
            type = NavType.StringType
            if(code != null){
                defaultValue = code
            }
        })
    }
}