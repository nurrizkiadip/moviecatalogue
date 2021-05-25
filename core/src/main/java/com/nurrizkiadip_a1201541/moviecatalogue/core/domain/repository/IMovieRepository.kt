package com.nurrizkiadip_a1201541.moviecatalogue.core.domain.repository

import com.nurrizkiadip_a1201541.moviecatalogue.core.data.Resource
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    //get All data from server
    fun getAllMovies(): Flow<Resource<List<Movie>>>
    fun getAllTvShow(): Flow<Resource<List<TvShow>>>
    fun getMoviesBySearch(search: String): Flow<List<Movie>>
    fun getTvsBySearch(search: String): Flow<List<TvShow>>

    //favorite
    fun getAllMoviesFavorite(): Flow<List<Movie>>
    fun getAllTvFavorite(): Flow<List<TvShow>>
    fun getMoviesFavBySearch(search: String): Flow<List<Movie>>
    fun getTvsFavBySearch(search: String): Flow<List<TvShow>>
    fun setMovieToFavorite(movie: Movie, newState: Boolean)
    fun setTvToFavorite(tvShow: TvShow, newState: Boolean)

}