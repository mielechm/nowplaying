package com.mielechm.nowplaying.repository

import com.mielechm.nowplaying.data.FavoritesDao
import com.mielechm.nowplaying.data.entities.Favorite
import com.mielechm.nowplaying.data.remote.MoviesApi
import com.mielechm.nowplaying.data.remote.response.MovieDetailsResponse
import com.mielechm.nowplaying.data.remote.response.MoviesResponse
import com.mielechm.nowplaying.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class DefaultMoviesRepository @Inject constructor(
    private val api: MoviesApi,
    private val dao: FavoritesDao
) : MoviesRepository {
    override suspend fun getMoviesNowPlayingPaginated(page: Int): Resource<MoviesResponse> {
        val response = try {
            api.getMoviesNowPlayingPaginated(page)
        } catch (e: Exception) {
            return Resource.Error("Error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }

    override suspend fun searchForMovies(query: String): Resource<MoviesResponse> {
        val response = try {
            api.searchForMovie(query)
        } catch (e: Exception) {
            return Resource.Error("Error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }

    override suspend fun getMovieDetails(id: Int): Resource<MovieDetailsResponse> {
        val response = try {
            api.getMovieDetails(id)
        } catch (e: Exception) {
            return Resource.Error("Error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }

    override fun getFavoriteMovies(): Flow<List<Favorite>> =
        dao.getAllFavorites()

    override suspend fun addToFavorites(favorite: Favorite) {
        dao.insertToFavorites(favorite)
    }

    override suspend fun removeFromFavorites(favorite: Favorite) {
        dao.deleteFromFavorites(favorite)
    }
}