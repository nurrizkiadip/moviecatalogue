package com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movieentities")
    fun getAllMovieFavorite(): DataSource.Factory<Int, MovieEntity>

    @Query("SELECT * FROM tvshowentities")
    fun getAllTvFavorite(): DataSource.Factory<Int, TvShowEntity>

    @Query("SELECT * FROM movieentities WHERE title LIKE :search")
    fun getMoviesFavBySearch(search: String): DataSource.Factory<Int, MovieEntity>

    @Query("SELECT * FROM tvshowentities WHERE name LIKE :search")
    fun getTvsFavBySearch(search: String): DataSource.Factory<Int, TvShowEntity>

    @Query("SELECT * FROM movieentities WHERE id=:id")
    fun getMovieFavById(id: Int): LiveData<MovieEntity>

    @Query("SELECT * FROM tvshowentities WHERE id=:id")
    fun getTvFavById(id: Int): LiveData<TvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieFav(movieFav: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShowFav(tvFav: TvShowEntity)

    @Delete
    fun deleteTvFav(tv: TvShowEntity)

    @Delete
    fun deleteMovieFav(movie: MovieEntity)

    /*FOR TESTING*/
    @Query("SELECT * FROM movieentities")
    fun getAllMovieFavoriteLiveData(): LiveData<MovieEntity>

    @Query("SELECT * FROM tvshowentities")
    fun getAllTvFavoriteLiveData(): LiveData<TvShowEntity>
}