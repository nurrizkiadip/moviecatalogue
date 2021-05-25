package com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tvshowentities")
data class TvShowEntity(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var tvId: String,

        @ColumnInfo(name = "poster_path")
        var posterPath: String? = null,

        @ColumnInfo(name = "name")
        var title: String? = null,

        @ColumnInfo(name = "overview")
        var overview: String? = null,

        @ColumnInfo(name = "popularity")
        var popularity: String? = null,

        @ColumnInfo(name = "first_air_date")
        var firstAirDate: String? = null,

        @ColumnInfo(name = "number_of_episodes")
        var numberOfEpisodes: Int? = null,

        @ColumnInfo(name = "number_of_seasons")
        var numberOfSeasons: Int? = null,

        @ColumnInfo(name = "original_language")
        var originalLanguage: String? = null,

        @ColumnInfo(name = "isFavorite")
        var isFavorite: Boolean = false
)