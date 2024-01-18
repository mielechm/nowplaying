package com.mielechm.nowplaying.features.moviesnowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Theaters
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.mielechm.nowplaying.data.model.MovieListEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesNowPlayingScreen(navController: NavController) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Now Playing") })
        }) {
            Column(modifier = Modifier.padding(it)) {
                MoviesNowPlayingList(navController)
            }
        }
    }
}

@Composable
fun MoviesNowPlayingList(
    navController: NavController,
    viewModel: MoviesNowPlayingViewModel = hiltViewModel()
) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()
    val end by viewModel.end.collectAsState()

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (movies.size % 2 == 0) {
            movies.size / 2
        } else {
            movies.size / 2 + 1
        }
        items(itemCount) {
            if (it >= itemCount - 1 && !end && !isLoading) {
                LaunchedEffect(key1 = true) {
                    viewModel.getNowPlayingPaginated()
                }
            }
            MoviesRow(rowIndex = it, movies = movies, navController = navController)
        }

    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (loadError.isNotEmpty()) {
            Text(
                text = loadError,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun MovieEntry(movie: MovieListEntry, navController: NavController, modifier: Modifier = Modifier) {

    Box(contentAlignment = Alignment.Center, modifier = modifier.clickable {
        navController.navigate("movie_details/${movie.id}")
    }) {

        Column {
            if (movie.poster != "https://image.tmdb.org/t/p/originalnull") {
                SubcomposeAsyncImage(
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    model = movie.poster,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                )
            } else {

                Image(
                    imageVector = Icons.Outlined.Theaters,
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                        .size(120.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}

@Composable
fun MoviesRow(rowIndex: Int, movies: List<MovieListEntry>, navController: NavController) {
    Column {
        Row {
            MovieEntry(
                movie = movies[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (movies.size >= rowIndex * 2 + 2) {
                MovieEntry(
                    movie = movies[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}