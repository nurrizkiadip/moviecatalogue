package com.nurrizkiadip_a1201541.moviecatalogue.core.domain.usecase

import com.nurrizkiadip_a1201541.moviecatalogue.core.data.Resource
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(
    private val repository: IMovieRepository
) : MovieUseCase{

    /*Home*/
    override fun getAllMovies(): Flow<Resource<List<Movie>>> {
        return repository.getAllMovies()
    }
    override fun getAllTvShow(): Flow<Resource<List<TvShow>>> {
        return repository.getAllTvShow()
    }
    override fun getMoviesBySearch(search: String): Flow<List<Movie>> {
        return repository.getMoviesBySearch(search)
    }
    override fun getTvsBySearch(search: String): Flow<List<TvShow>> {
        return repository.getTvsBySearch(search)
    }

    /*Favorite*/
    override fun getAllMoviesFavorite(): Flow<List<Movie>> {
        return repository.getAllMoviesFavorite()
    }
    override fun getAllTvFavorite(): Flow<List<TvShow>> {
        return repository.getAllTvFavorite()
    }
    override fun getMoviesFavBySearch(search: String): Flow<List<Movie>> {
        return repository.getMoviesFavBySearch(search)
    }
    override fun getTvsFavBySearch(search: String): Flow<List<TvShow>> {
        return repository.getTvsFavBySearch(search)
    }
    override fun setMovieToFavorite(movie: Movie, newState: Boolean) {
        return repository.setMovieToFavorite(movie, newState)
    }
    override fun setTvToFavorite(tvShow: TvShow, newState: Boolean) {
        return repository.setTvToFavorite(tvShow, newState)
    }

}