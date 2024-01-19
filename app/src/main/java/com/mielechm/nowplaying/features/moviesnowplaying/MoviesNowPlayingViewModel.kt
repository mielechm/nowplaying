package com.mielechm.nowplaying.features.moviesnowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.nowplaying.data.entities.Favorite
import com.mielechm.nowplaying.data.model.MovieListEntry
import com.mielechm.nowplaying.repository.DefaultMoviesRepository
import com.mielechm.nowplaying.utils.IMAGE_BASE_URL
import com.mielechm.nowplaying.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MoviesNowPlayingViewModel @Inject constructor(private val repository: DefaultMoviesRepository) :
    ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MovieListEntry>>(listOf())
    val searchResults = _searchResults.asStateFlow()

    private val _movies = MutableStateFlow<List<MovieListEntry>>(listOf())
    val movies = searchText.debounce(500L).onEach { _isSearching.update { true } }.combine(_movies) { text, movies ->
        if (text.isBlank()) {
            movies
        } else {
            movies.filter {
                it.title.contains(text, ignoreCase = true)
            }
        }
    }.onEach { _isSearching.update { false } }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _movies.value)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow("")
    val loadError = _loadError.asStateFlow()

    private val _end = MutableStateFlow(false)
    val end = _end.asStateFlow()
    private var currentPage = 1

    private val _favorites = MutableStateFlow<List<Favorite>>(listOf())

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

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
                            vote = it.voteAverage,
                            isFavorite = _favorites.value.contains(Favorite(it.id))
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

    fun getFavorites() {
        viewModelScope.launch {
            repository.getFavoriteMovies().flowOn(Dispatchers.IO).collect {
                _favorites.value = it
                val temp = _movies.value

                for (movie in temp) {
                    movie.isFavorite = _favorites.value.contains(Favorite(movie.id))
                }

                _movies.value = temp
            }

        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}