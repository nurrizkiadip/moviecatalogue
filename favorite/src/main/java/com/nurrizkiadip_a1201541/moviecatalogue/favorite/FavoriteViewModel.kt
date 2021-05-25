package com.nurrizkiadip_a1201541.moviecatalogue.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.usecase.MovieUseCase

class FavoriteViewModel constructor(
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    val allMovieFavorite: LiveData<List<Movie>> = movieUseCase.getAllMoviesFavorite().asLiveData()
    val allTvFavorite: LiveData<List<TvShow>> = movieUseCase.getAllTvFavorite().asLiveData()

    fun getMoviesBySearch(search: String): LiveData<List<Movie>> {
        return movieUseCase.getMoviesFavBySearch(search).asLiveData()
    }

    fun getTvsBySearch(search: String): LiveData<List<TvShow>> {
        return movieUseCase.getTvsFavBySearch(search).asLiveData()
    }
}