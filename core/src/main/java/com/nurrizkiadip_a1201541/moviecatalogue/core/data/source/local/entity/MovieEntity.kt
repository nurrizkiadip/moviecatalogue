package com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movieentities")
data class MovieEntity(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var movieId: String,

        @ColumnInfo(name = "poster_path")
        var posterPath: String? = null,

        @ColumnInfo(name = "title")
        var title: String? = null,

        @ColumnInfo(name = "overview")
        var overview: String? = null,

        @ColumnInfo(name = "popularity")
        var popularity: String? = null,

        @ColumnInfo(name = "release_date")
        var releaseDate: String? = null,

        @ColumnInfo(name = "original_language")
        var originalLanguage: String? = null,

        @ColumnInfo(name = "isFavorite")
        var isFavorite: Boolean = false
)
