package com.nurrizkiadip_a1201541.moviecatalogue.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.nurrizkiadip_a1201541.moviecatalogue.data.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.data.Repository
import com.nurrizkiadip_a1201541.moviecatalogue.data.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity

class FavoriteViewModel(val repository: Repository) : ViewModel() {

    fun getAllMovieFavorite(): LiveData<PagedList<MovieEntity>> = repository.getAllMoviesFavorite()

    fun getAllTvFavorite(): LiveData<PagedList<TvShowEntity>> = repository.getAllTvFavorite()

    fun getMoviesBySearch(search: String): LiveData<PagedList<MovieEntity>> {
        return repository.getMoviesFavBySearch(search)
    }

    fun getTvsBySearch(search: String): LiveData<PagedList<TvShowEntity>> {
        return repository.getTvsFavBySearch(search)
    }

    fun deleteMovieFavorite(movieEntity: MovieEntity) {
        val movie = Movie(
            movieEntity.movieId,
            movieEntity.posterPath,
            movieEntity.title,
            movieEntity.overview,
            movieEntity.popularity,
            movieEntity.releaseDate,
            movieEntity.originalLanguage,
            movieEntity.isFavorite
        )
        repository.deleteMovieFav(movie)
    }

    fun deleteTvFavorite(tvShowEntity: TvShowEntity) {
        val tv = TvShow(
            tvShowEntity.tvId,
            tvShowEntity.posterPath,
            tvShowEntity.title,
            tvShowEntity.overview,
            tvShowEntity.popularity,
            tvShowEntity.firstAirDate,
            tvShowEntity.numberOfEpisodes,
            tvShowEntity.numberOfSeasons,
            tvShowEntity.originalLanguage,
            tvShowEntity.isFavorite
        )
        repository.deleteTvShowFav(tv)
    }

}