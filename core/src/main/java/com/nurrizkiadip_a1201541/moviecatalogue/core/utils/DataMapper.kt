package com.nurrizkiadip_a1201541.moviecatalogue.core.utils

import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.MovieDetailResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.TvResultsItem
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow

object DataMapper {
    fun mapMovieResponsesToEntities(input: List<MovieDetailResponse>): List<MovieEntity> {
        val tourismList = ArrayList<MovieEntity>()
        input.map {
            val tourism = MovieEntity(
                it.id.toString(),
                it.posterPath,
                it.title,
                it.overview,
                it.popularity.toString(),
                it.releaseDate,
                it.originalLanguage,
                isFavorite = false
            )
            tourismList.add(tourism)
        }
        return tourismList
    }

    fun mapTvResponsesToEntities(input: List<TvDetailResponse>): List<TvShowEntity> {
        val tourismList = ArrayList<TvShowEntity>()
        input.map {
            val tourism = TvShowEntity(
                it.id.toString(),
                it.posterPath,
                it.name,
                it.overview,
                it.popularity.toString(),
                it.firstAirDate,
                it.numberOfEpisodes,
                it.numberOfSeasons,
                it.originalLanguage,
                isFavorite = false
            )
            tourismList.add(tourism)
        }
        return tourismList
    }

    //fix
    fun mapTvResultItemToTvDetailResponse(input: List<TvResultsItem>): List<TvDetailResponse> {
        val tourismList = ArrayList<TvDetailResponse>()
        input.map {
            val tourism = TvDetailResponse(
                id = it.id,
                posterPath = it.posterPath,
                name = it.name,
                overview = it.overview,
                originalLanguage = it.originalLanguage,
                popularity = it.popularity,
                numberOfEpisodes = 0,
                numberOfSeasons = 0,
                firstAirDate = it.firstAirDate
            )
            tourismList.add(tourism)
        }
        return tourismList
    }

    //get data from database
    fun mapMovieEntitiesToDomain(input: List<MovieEntity>): List<Movie> =
        input.map {
            Movie(
                it.movieId, it.posterPath, it.title,
                it.overview, it.popularity, it.releaseDate,
                it.originalLanguage, it.isFavorite,
            )
        }

    //get data from database
    fun mapTvEntitiesToDomain(input: List<TvShowEntity>): List<TvShow> =
        input.map {
            TvShow(
                it.tvId, it.posterPath, it.title,
                it.overview, it.popularity, it.firstAirDate,
                it.numberOfEpisodes, it.numberOfSeasons,
                it.originalLanguage, it.isFavorite,
            )
        }

    //to save data in database
    fun mapMovieDomainToEntity(input: Movie) = MovieEntity(
        input.movieId, input.posterPath, input.title,
        input.overview, input.popularity, input.releaseDate,
        input.originalLanguage, input.isFavorite,
    )

    //to save data in database
    fun mapTvDomainToEntity(input: TvShow) = TvShowEntity(
        input.tvId, input.posterPath, input.title,
        input.overview, input.popularity, input.firstAirDate,
        input.numberOfEpisodes, input.numberOfSeasons,
        input.originalLanguage, input.isFavorite,
    )
}