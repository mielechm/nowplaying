package com.mielechm.nowplaying

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mielechm.nowplaying.features.moviedetails.MovieDetailsScreen
import com.mielechm.nowplaying.features.moviesnowplaying.MoviesNowPlayingScreen
import com.mielechm.nowplaying.ui.theme.NowPlayingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NowPlayingTheme {
                // A surface container using the 'background' color from the theme

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "movies_now_playing"
                ) {
                    composable("movies_now_playing") {
                        MoviesNowPlayingScreen(navController = navController)
                    }
                    composable(
                        "movie_details/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) {
                        val id = remember {
                            val id = it.arguments?.getInt("id")
                            id ?: 0
                        }
                        MovieDetailsScreen(id = id, navController = navController)
                    }
                }

            }
        }
    }
}
