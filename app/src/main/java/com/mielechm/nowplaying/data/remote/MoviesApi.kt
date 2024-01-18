package com.mielechm.nowplaying.data.remote

import com.mielechm.nowplaying.data.remote.response.MovieDetailsResponse
import com.mielechm.nowplaying.data.remote.response.MoviesNowPlaying
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("now_playing")
    suspend fun getMoviesNowPlayingPaginated(@Query("page") page: Int): MoviesNowPlaying

    @GET("{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetailsResponse
}