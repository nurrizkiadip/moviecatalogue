package com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
        var movieId: String,
        var posterPath: String? = null,
        var title: String? = null,
        var overview: String? = null,
        var popularity: String? = null,
        var releaseDate: String? = null,
        var originalLanguage: String? = null,
        var isFavorite: Boolean = false
) : Parcelable
