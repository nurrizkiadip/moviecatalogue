package com.nurrizkiadip_a1201541.moviecatalogue.detail

import androidx.lifecycle.*
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.usecase.MovieUseCase

class DetailViewModel constructor(
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    fun setMovieFavorite(movie: Movie, newState: Boolean){
        movieUseCase.setMovieToFavorite(movie, newState)
    }

    fun setTvFavorite(tv: TvShow, newState: Boolean){
        movieUseCase.setTvToFavorite(tv, newState)
    }

}
