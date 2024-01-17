package com.mielechm.nowplaying.features.moviesnowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.nowplaying.data.model.MovieListEntry
import com.mielechm.nowplaying.repository.DefaultMoviesRepository
import com.mielechm.nowplaying.utils.IMAGE_BASE_URL
import com.mielechm.nowplaying.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesNowPlayingViewModel @Inject constructor(private val repository: DefaultMoviesRepository) :
    ViewModel() {

    private val _movies = MutableStateFlow<List<MovieListEntry>>(listOf())
    val movies = _movies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow("")
    val loadError = _loadError.asStateFlow()

    private val _end = MutableStateFlow(false)
    val end = _end.asStateFlow()
    private var currentPage = 1

    init {
        currentPage = 1
        getNowPlayingPaginated()
    }

    fun getNowPlayingPaginated() {

        viewModelScope.launch {

            _isLoading.value = true

            when (val result = repository.getMoviesNowPlayingPaginated(currentPage)) {
                is Resource.Error -> {
                    _loadError.value = result.message.toString()
                    _isLoading.value = false
                }

                is Resource.Success -> {
                    val moviesEntries = result.data!!.movies.map {
                        MovieListEntry(
                            id = it.id,
                            title = it.title,
                            overview = it.overview,
                            poster = "$IMAGE_BASE_URL${it.posterPath}",
                            vote = it.voteAverage
                        )
                    }

                    if (currentPage < result.data.totalPages) {
                        currentPage++
                    } else {
                        _end.value = true
                    }

                    _loadError.value = ""
                    _isLoading.value = false
                    _movies.value += moviesEntries
                }

            }
        }

    }
}