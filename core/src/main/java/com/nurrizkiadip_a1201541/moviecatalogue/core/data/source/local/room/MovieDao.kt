package com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.room

import androidx.room.*
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity.TvShowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movieentities")
    fun getAllMovie(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM tvshowentities")
    fun getAllTv(): Flow<List<TvShowEntity>>

    @Query("SELECT * FROM movieentities WHERE title LIKE :search")
    fun getMoviesBySearch(search: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM tvshowentities WHERE name LIKE :search")
    fun getTvsBySearch(search: String): Flow<List<TvShowEntity>>

    @Query("SELECT * FROM movieentities WHERE isFavorite = 1")
    fun getAllMovieFavorite(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM tvshowentities WHERE isFavorite = 1")
    fun getAllTvFavorite(): Flow<List<TvShowEntity>>

    @Query("SELECT * FROM movieentities WHERE title LIKE :search AND isFavorite = 1")
    fun getMoviesFavBySearch(search: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM tvshowentities WHERE name LIKE :search AND isFavorite = 1")
    fun getTvsFavBySearch(search: String): Flow<List<TvShowEntity>>


    //===============================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovie(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTvShow(tvs: List<TvShowEntity>)

    //update to set favorite
    @Update
    suspend fun updateMovie(movie: MovieEntity)

    //update to set favorite
    @Update
    suspend fun updateTv(tv: TvShowEntity)


//    /*FOR TESTING*/
//    @Query("SELECT * FROM movieentities")
//    fun getAllMovieFavoriteLiveData(): LiveData<MovieEntity>
//
//    @Query("SELECT * FROM tvshowentities")
//    fun getAllTvFavoriteLiveData(): LiveData<TvShowEntity>
}