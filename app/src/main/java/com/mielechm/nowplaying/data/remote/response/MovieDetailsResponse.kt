package com.mielechm.nowplaying.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val budget: Int,
    val genres: List<Genre>,
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)