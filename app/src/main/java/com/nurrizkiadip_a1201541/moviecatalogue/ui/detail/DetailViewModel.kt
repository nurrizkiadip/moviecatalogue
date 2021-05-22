package com.nurrizkiadip_a1201541.moviecatalogue.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.data.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.data.Repository
import com.nurrizkiadip_a1201541.moviecatalogue.data.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse

class DetailViewModel (private val repository: Repository) : ViewModel() {
    var id = MutableLiveData<String>()
    var movieDetail = MutableLiveData<ApiResponse<Movie>>()
    var tvShowDetail = MutableLiveData<ApiResponse<TvShow>>()

    fun setId(idData: String){
        id.value = idData
    }

    fun getMovieDetail(id: String){
        movieDetail = repository.getMovieById(id.toInt()) as MutableLiveData<ApiResponse<Movie>>
    }

    var movieFavDetail: LiveData<MovieEntity> = Transformations.switchMap(id){
        repository.getMovieFavById(it.toInt())
    }

    fun getTvShowDetail(id: String){
        tvShowDetail = repository.getTvById(id.toInt()) as MutableLiveData<ApiResponse<TvShow>>
    }

    var tvShowFavDetail: LiveData<TvShowEntity> = Transformations.switchMap(id){
        repository.getTvFavById(it.toInt())
    }

    fun setMovieFavorite(movie: Movie){
        movie.isFavorite = true
        repository.setMovieToFavorite(movie)
    }

    fun setTvFavorite(tv: TvShow){
        tv.isFavorite = true
        repository.setTvToFavorite(tv)
    }

    fun deleteMovieFav(movie: Movie){
        repository.deleteMovieFav(movie)
    }

    fun deleteTvFav(tv: TvShow) {
        repository.deleteTvShowFav(tv)
    }
}
