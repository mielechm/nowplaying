package com.mielechm.nowplaying.repository

import com.mielechm.nowplaying.data.remote.MoviesApi
import com.mielechm.nowplaying.data.remote.response.MoviesNowPlaying
import com.mielechm.nowplaying.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultMoviesRepository @Inject constructor(private val api: MoviesApi): MoviesRepository {
    override suspend fun getMoviesNowPlayingPaginated(page: Int): Resource<MoviesNowPlaying> {
        val response = try {
            api.getMoviesNowPlayingPaginated(page)
        } catch (e: Exception) {
            return Resource.Error("Error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }
}