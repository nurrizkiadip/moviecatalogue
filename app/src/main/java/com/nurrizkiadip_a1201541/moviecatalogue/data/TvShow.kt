package com.nurrizkiadip_a1201541.moviecatalogue.data


data class TvShow(
        var tvId: String,
        var posterPath: String? = null,
        var title: String? = null,
        var overview: String? = null,
        var popularity: String? = null,
        var firstAirDate: String? = null,
        var numberOfEpisodes: Int? = null,
        var numberOfSeasons: Int? = null,
        var originalLanguage: String? = null,
        var isFavorite: Boolean = false
)