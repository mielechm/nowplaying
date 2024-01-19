package com.mielechm.nowplaying.data.remote

import com.mielechm.nowplaying.data.remote.response.MovieDetailsResponse
import com.mielechm.nowplaying.data.remote.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlayingPaginated(@Query("page") page: Int): MoviesResponse

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetailsResponse

    @GET("search/movie")
    suspend fun searchForMovie(@Query("query") query: String): MoviesResponse
}