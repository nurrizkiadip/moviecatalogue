package com.nurrizkiadip_a1201541.moviecatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.room.MovieDao

class LocalDataSource(private val movieDao: MovieDao) {

    companion object{
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(movieDao: MovieDao): LocalDataSource{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: LocalDataSource(movieDao).apply { INSTANCE = this }
            }
        }
    }

    fun getAllMoviesFavorite(): DataSource.Factory<Int, MovieEntity> = movieDao.getAllMovieFavorite()

    fun getAllTvFavorite(): DataSource.Factory<Int, TvShowEntity> = movieDao.getAllTvFavorite()

    fun getMoviesFavBySearch(search: String): DataSource.Factory<Int, MovieEntity> = movieDao.getMoviesFavBySearch(search)

    fun getTvsFavBySearch(search: String): DataSource.Factory<Int, TvShowEntity> = movieDao.getTvsFavBySearch(search)

    fun getMovieFavById(id: Int): LiveData<MovieEntity> = movieDao.getMovieFavById(id)

    fun getTvFavById(id: Int): LiveData<TvShowEntity> = movieDao.getTvFavById(id)

    fun setMovieFav(movie: MovieEntity) {
        movieDao.insertMovieFav(movie)
    }

    fun setTvFav(tv: TvShowEntity) {
        movieDao.insertTvShowFav(tv)
    }

    fun deleteMovieFav(movie: MovieEntity) {
        movieDao.deleteMovieFav(movie)
    }

    fun deleteTvFav(tv: TvShowEntity) {
        movieDao.deleteTvFav(tv)
    }
}