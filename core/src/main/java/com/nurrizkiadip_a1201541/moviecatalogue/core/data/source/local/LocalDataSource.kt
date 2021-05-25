package com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local

import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val movieDao: MovieDao) {

    fun getAllMovies(): Flow<List<MovieEntity>> = movieDao.getAllMovie()
    fun getAllTv(): Flow<List<TvShowEntity>> = movieDao.getAllTv()
    fun getMoviesBySearch(search: String): Flow<List<MovieEntity>> = movieDao.getMoviesBySearch(search)
    fun getTvsBySearch(search: String): Flow<List<TvShowEntity>> = movieDao.getTvsBySearch(search)

    //favorite
    fun getAllMoviesFavorite(): Flow<List<MovieEntity>> = movieDao.getAllMovieFavorite()
    fun getAllTvFavorite(): Flow<List<TvShowEntity>> = movieDao.getAllTvFavorite()
    fun getMoviesFavBySearch(search: String): Flow<List<MovieEntity>> = movieDao.getMoviesFavBySearch(search)
    fun getTvsFavBySearch(search: String): Flow<List<TvShowEntity>> = movieDao.getTvsFavBySearch(search)


    //===================================================
    suspend fun insertAllMovie(movie: List<MovieEntity>) = movieDao.insertAllMovie(movie)
    suspend fun insertAllTvShow(tv: List<TvShowEntity>) = movieDao.insertAllTvShow(tv)

    suspend fun setMovieToFav(movie: MovieEntity, newState: Boolean) {
        movie.isFavorite = newState
        movieDao.updateMovie(movie)
    }

    suspend fun setTvToFav(tv: TvShowEntity, newState: Boolean) {
        tv.isFavorite = newState
        movieDao.updateTv(tv)
    }
}