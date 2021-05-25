package com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity.TvShowEntity

@Database(entities = [MovieEntity::class, TvShowEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao

}