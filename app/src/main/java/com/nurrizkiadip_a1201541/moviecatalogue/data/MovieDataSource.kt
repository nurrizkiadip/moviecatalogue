package com.nurrizkiadip_a1201541.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse

interface MovieDataSource {
    fun getAllMovies(): LiveData<ApiResponse<List<Movie>>>
    fun getAllTvShow(): LiveData<ApiResponse<List<TvShow>>>
    fun getMovieById(id: Int): LiveData<ApiResponse<Movie>>
    fun getTvById(id: Int): LiveData<ApiResponse<TvShow>>

    fun getAllMoviesFavorite(): LiveData<PagedList<MovieEntity>>
    fun getAllTvFavorite(): LiveData<PagedList<TvShowEntity>>

    fun setMovieToFavorite(movie: Movie)
    fun setTvToFavorite(tvShow: TvShow)
    fun getMoviesFavBySearch(search: String): LiveData<PagedList<MovieEntity>>
    fun getTvsFavBySearch(search: String): LiveData<PagedList<TvShowEntity>>
    fun getMovieFavById(id: Int): LiveData<MovieEntity>
    fun getTvFavById(id: Int): LiveData<TvShowEntity>
    fun deleteTvShowFav(tvShow: TvShow)
    fun deleteMovieFav(movie: Movie)
}