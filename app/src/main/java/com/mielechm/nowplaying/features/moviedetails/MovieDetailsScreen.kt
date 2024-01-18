package com.mielechm.nowplaying.features.moviedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Theaters
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    id: Int,
    navController: NavController,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {

    val movie by viewModel.movieDetails.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    viewModel.getMovieDetails(id)

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(movie.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.addToFavorites(movie.id) }) {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Filled.Star
                            } else {
                                Icons.Outlined.StarBorder
                            },
                            contentDescription = "Favorite"
                        )

                    }
                }
            )
        }) {
            Column(modifier = Modifier.padding(it)) {
                MovieDetailsData(viewModel)
            }
        }
    }
}

@Composable
fun MovieDetailsData(viewModel: MovieDetailsViewModel) {
    val movie by viewModel.movieDetails.collectAsState()
    val loadError by viewModel.loadError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        if (movie.posterPath != "https://image.tmdb.org/t/p/originalnull") {
            SubcomposeAsyncImage(
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                model = movie.posterPath,
                contentDescription = movie.title,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
        } else if (movie.backdropPath != "https://image.tmdb.org/t/p/originalnull") {
            SubcomposeAsyncImage(
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                model = movie.backdropPath,
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
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Original title: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = movie.originalTitle)
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Release date: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = movie.releaseDate)
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Runtime: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = "${movie.runtime} minutes")
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Rating: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = "${movie.voteAverage} out of ${movie.voteCount} votes")
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Overview:\n")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = movie.overview)
                }
            },
            textAlign = TextAlign.Justify, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
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