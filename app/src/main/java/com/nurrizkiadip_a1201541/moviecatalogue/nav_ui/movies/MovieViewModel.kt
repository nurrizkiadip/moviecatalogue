package com.nurrizkiadip_a1201541.moviecatalogue.nav_ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.Resource
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.usecase.MovieUseCase

class MovieViewModel constructor(
    private val movieUseCase: MovieUseCase
): ViewModel() {

    val listMovies: LiveData<Resource<List<Movie>>> = movieUseCase.getAllMovies().asLiveData()

    fun getListMoviesSearch(search: String): LiveData<List<Movie>> {
        return movieUseCase.getMoviesFavBySearch(search).asLiveData()
    }

}
