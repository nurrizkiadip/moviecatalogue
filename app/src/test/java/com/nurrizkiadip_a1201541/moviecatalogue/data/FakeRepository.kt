package com.nurrizkiadip_a1201541.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.LocalDataSource
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FakeRepository(
    private val remoteData: RemoteDataSource,
    private val localData: LocalDataSource,
    private val coroutineScope: CoroutineScope
): MovieDataSource {

    override fun getAllMovies(): LiveData<ApiResponse<List<Movie>>> {
        var movieList = MutableLiveData<ApiResponse<List<Movie>>>()

        remoteData.getAllMovies(object: RemoteDataSource.LoadAllMoviesCallback{
            override fun onAllMoviesReceived(movieResponse: MutableLiveData<ApiResponse<List<Movie>>>) {
                movieList = movieResponse
            }
        })

        return movieList
    }

    override fun getAllTvShow(): LiveData<ApiResponse<List<TvShow>>> {
        var tvList = MutableLiveData<ApiResponse<List<TvShow>>>()

        remoteData.getAllTvShows(object: RemoteDataSource.LoadAllTvsCallback{
            override fun onAllTvsReceived(tvResponse: MutableLiveData<ApiResponse<List<TvShow>>>) {
                tvList = tvResponse
            }
        })

        return tvList
    }

    override fun getMovieById(id: Int): LiveData<ApiResponse<Movie>> {
        var movie = MutableLiveData<ApiResponse<Movie>>()

        remoteData.getMovieById(id, object: RemoteDataSource.LoadMovieByIdCallback{
            override fun onMovieByIdReceived(movieResponse: MutableLiveData<ApiResponse<Movie>>) {
                movie = movieResponse
            }
        })

        return movie
    }

    override fun getTvById(id: Int): LiveData<ApiResponse<TvShow>> {
        var tv = MutableLiveData<ApiResponse<TvShow>>()

        remoteData.getTvById(id, object: RemoteDataSource.LoadTvByIdCallback{
            override fun onTvByIdReceived(tvResponse: MutableLiveData<ApiResponse<TvShow>>) {
                tv = tvResponse
            }
        })

        return tv
    }

    override fun getAllMoviesFavorite(): LiveData<PagedList<MovieEntity>> {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localData.getAllMoviesFavorite(), config).build()
    }


    override fun getAllTvFavorite(): LiveData<PagedList<TvShowEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localData.getAllTvFavorite(), config).build()
    }

    override fun getMovieFavById(id: Int): LiveData<MovieEntity> {
        return localData.getMovieFavById(id)
    }

    override fun getTvFavById(id: Int): LiveData<TvShowEntity> {
        return localData.getTvFavById(id)
    }

    override fun getMoviesFavBySearch(search: String): LiveData<PagedList<MovieEntity>> {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localData.getMoviesFavBySearch(search), config).build()
    }

    override fun getTvsFavBySearch(search: String): LiveData<PagedList<TvShowEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localData.getTvsFavBySearch(search), config).build()
    }

    override fun setMovieToFavorite(movie: Movie) {
        coroutineScope.launch(Dispatchers.IO) {
            val movieEntity = MovieEntity(
                movie.movieId,
                movie.posterPath,
                movie.title,
                movie.overview,
                movie.popularity,
                movie.releaseDate,
                movie.originalLanguage,
                movie.isFavorite
            )
            localData.setMovieFav(movieEntity)
        }
    }

    override fun setTvToFavorite(tvShow: TvShow) {
        coroutineScope.launch(Dispatchers.IO) {
            val tvEntity = TvShowEntity(
                tvShow.tvId,
                tvShow.posterPath,
                tvShow.title,
                tvShow.overview,
                tvShow.popularity,
                tvShow.firstAirDate,
                tvShow.numberOfEpisodes,
                tvShow.numberOfSeasons,
                tvShow.originalLanguage,
                tvShow.isFavorite
            )
            localData.setTvFav(tvEntity)
        }
    }

    override fun deleteMovieFav(movie: Movie) {
        coroutineScope.launch(Dispatchers.IO) {
            val movieEntity = MovieEntity(
                movie.movieId,
                movie.posterPath,
                movie.title,
                movie.overview,
                movie.popularity,
                movie.releaseDate,
                movie.originalLanguage,
                movie.isFavorite
            )
            localData.deleteMovieFav(movieEntity)
        }
    }

    override fun deleteTvShowFav(tvShow: TvShow) {
        coroutineScope.launch(Dispatchers.IO){
            val tvEntity = TvShowEntity(
                tvShow.tvId,
                tvShow.posterPath,
                tvShow.title,
                tvShow.overview,
                tvShow.popularity,
                tvShow.firstAirDate,
                tvShow.numberOfEpisodes,
                tvShow.numberOfSeasons,
                tvShow.originalLanguage,
                tvShow.isFavorite
            )
            localData.deleteTvFav(tvEntity)
        }
    }
}