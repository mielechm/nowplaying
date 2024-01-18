package com.mielechm.nowplaying.features.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.nowplaying.data.model.MovieDetails
import com.mielechm.nowplaying.repository.DefaultMoviesRepository
import com.mielechm.nowplaying.utils.IMAGE_BASE_URL
import com.mielechm.nowplaying.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val repository: DefaultMoviesRepository) :
    ViewModel() {

    private val _movieDetails = MutableStateFlow<MovieDetails>(MovieDetails())
    val movieDetails = _movieDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow("")
    val loadError = _loadError.asStateFlow()

    fun getMovieDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            when (val result = repository.getMovieDetails(id)) {
                is Resource.Error -> {
                    _loadError.value = result.message.toString()
                    _isLoading.value = false
                }

                is Resource.Success -> {
                    val movie =
                        MovieDetails(
                            id = result.data!!.id,
                            backdropPath = "${IMAGE_BASE_URL}${result.data.backdropPath}",
                            posterPath = "${IMAGE_BASE_URL}${result.data.posterPath}",
                            releaseDate = result.data.releaseDate,
                            originalTitle = result.data.originalTitle,
                            overview = result.data.overview,
                            runtime = result.data.runtime,
                            title = result.data.title,
                            voteAverage = result.data.voteAverage,
                            voteCount = result.data.voteCount
                        )

                    _loadError.value = ""
                    _isLoading.value = false
                    _movieDetails.value = movie
                }
            }
        }
    }
}