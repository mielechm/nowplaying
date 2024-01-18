package com.mielechm.nowplaying.data.model

import com.mielechm.nowplaying.data.remote.response.Genre

data class MovieDetails(
    val backdropPath: String = "",
    val genres: List<Genre> = emptyList(),
    val id: Int = 0,
    val originalTitle: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val releaseDate: String = "",
    val runtime: Int = 0,
    val title: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
)
