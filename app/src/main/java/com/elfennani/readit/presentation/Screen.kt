package com.elfennani.readit.presentation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.elfennani.readit.domain.model.Gallery
import com.google.gson.Gson
import java.net.URLEncoder

sealed class Screen(val route: String, val navArgs: List<NamedNavArgument> = emptyList()) {
    data object LoginScreen : Screen(route = "login")

    data object HomePage :
        Screen(
            route = "home/{userId}",
            navArgs = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
        fun createRoute(userId: String) = "home/$userId"

        fun createArgs(userId: String?) =
            listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    if (userId != null) {
                        defaultValue = userId
                    }
                }
            )
    }

    data object ExchangeToken :
        Screen(
            route = "exchange/{code}",
            navArgs = listOf(navArgument("code") { type = NavType.StringType })
        ) {
        fun createRoute(code: String) = "exchange/$code"

        fun createArgs(code: String?) =
            listOf(
                navArgument("code") {
                    type = NavType.StringType
                    if (code != null) {
                        defaultValue = code
                    }
                }
            )
    }

    data object PostScreen :
        Screen(
            route = "post/{id}",
            navArgs = listOf(navArgument("id") { type = NavType.StringType })
        ) {
        fun createRoute(id: String) = "post/$id"

        fun createArgs(id: String?) =
            listOf(
                navArgument("id") {
                    type = NavType.StringType
                    if (id != null) {
                        defaultValue = id
                    }
                }
            )
    }

    data object PostScreenWithInitialData :
        Screen(
            route = "post/{id}/{post}",
            navArgs =
                listOf(
                    navArgument("id") { type = NavType.StringType },
                    navArgument("post") { type = NavType.StringType }
                )
        ) {
        fun createRoute(id: String, post: String) = "post/$id/$post"
    }

    data object GalleryScreen: Screen(
        route = "gallery/{gallery}",
    ){
        fun createRoute(gallery: Gallery):String{
            val galleryEncoded = URLEncoder.encode(Gson().toJson(gallery, Gallery::class.java))

            return "gallery/$galleryEncoded"
        }
    }

    data object SavedScreen: Screen(
        route = "saved/{username}",
        navArgs = listOf(
            navArgument("username") {type = NavType.StringType}
        )
    )
}
