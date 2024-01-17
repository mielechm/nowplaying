package com.mielechm.nowplaying.data.remote.response

import com.google.gson.annotations.SerializedName

data class MoviesNowPlaying(
    val dates: Dates,
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalMovies: Int
)