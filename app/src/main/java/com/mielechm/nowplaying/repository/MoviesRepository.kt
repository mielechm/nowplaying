package com.mielechm.nowplaying.repository

import com.mielechm.nowplaying.data.remote.response.MoviesNowPlaying
import com.mielechm.nowplaying.utils.Resource

interface MoviesRepository {

    suspend fun getMoviesNowPlayingPaginated(page: Int): Resource<MoviesNowPlaying>

}