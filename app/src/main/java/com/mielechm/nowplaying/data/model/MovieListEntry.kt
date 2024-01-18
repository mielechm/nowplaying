package com.mielechm.nowplaying.data.model

data class MovieListEntry(
    val id: Int,
    val overview: String,
    val title: String,
    val vote: Double,
    val poster: String,
    var isFavorite: Boolean = false
)
