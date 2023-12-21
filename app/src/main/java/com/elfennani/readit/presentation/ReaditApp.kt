package com.elfennani.readit.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elfennani.readit.domain.model.Gallery
import com.elfennani.readit.presentation.auth.ExchangeTokenScreen
import com.elfennani.readit.presentation.gallery.GalleryScreen
import com.elfennani.readit.presentation.homefeed.HomeFeed
import com.elfennani.readit.presentation.login.LoginScreen
import com.elfennani.readit.presentation.postscreen.PostScreen
import com.elfennani.readit.presentation.saved.SavedScreen
import com.google.gson.Gson
import java.net.URLDecoder

@Composable
fun ReaditApp(code: String?, user: String?) {
    val navController = rememberNavController()
    val startRoute =
        when {
            code != null -> Screen.ExchangeToken.route
            user != null -> Screen.HomePage.route
            else -> Screen.LoginScreen.route
        }

    NavHost(
        navController = navController,
        startDestination = startRoute,
        enterTransition = {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left) +
                    fadeIn()
        },
        exitTransition = {
            slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right) +
                    fadeOut()
        }
    ) {
        composable(Screen.LoginScreen.route, Screen.LoginScreen.navArgs) { LoginScreen() }

        composable(
            Screen.HomePage.route,
            arguments = Screen.HomePage.createArgs(user),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            val userId = checkNotNull(it.arguments?.getString("userId"))
            HomeFeed(navController = navController)
        }

        composable(Screen.ExchangeToken.route, Screen.ExchangeToken.createArgs(code)) {
            ExchangeTokenScreen(code = code ?: "")
        }

        composable(Screen.PostScreen.route, Screen.PostScreen.navArgs) {
            PostScreen(navController = navController)
        }
        composable(
            Screen.PostScreenWithInitialData.route,
            Screen.PostScreenWithInitialData.navArgs
        ) {
            PostScreen(navController = navController)
        }
        composable(
            Screen.GalleryScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(500)
                )
            }
        ) {
            val galleryEncoded = checkNotNull(it.arguments?.getString("gallery"))
            val gallery = Gson().fromJson(URLDecoder.decode(galleryEncoded), Gallery::class.java)

            GalleryScreen(gallery = gallery)
        }

        composable(
            Screen.SavedScreen.route,
            Screen.SavedScreen.navArgs,
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
        ) {
            SavedScreen(navController)
        }
    }
}
