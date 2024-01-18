package com.mielechm.nowplaying.repository

import com.mielechm.nowplaying.data.entities.Favorite
import com.mielechm.nowplaying.data.remote.response.MovieDetailsResponse
import com.mielechm.nowplaying.data.remote.response.MoviesNowPlaying
import com.mielechm.nowplaying.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getMoviesNowPlayingPaginated(page: Int): Resource<MoviesNowPlaying>

    suspend fun getMovieDetails(id: Int): Resource<MovieDetailsResponse>

    fun getFavoriteMovies(): Flow<List<Favorite>>

    suspend fun addToFavorites(favorite: Favorite)

    suspend fun removeFromFavorites(favorite: Favorite)
}