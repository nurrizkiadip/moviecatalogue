package com.nurrizkiadip_a1201541.moviecatalogue.core.data

import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.LocalDataSource
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.ApiResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.RemoteDataSource
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.MovieDetailResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.repository.IMovieRepository
import com.nurrizkiadip_a1201541.moviecatalogue.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MovieRepository(
    private val remoteData: RemoteDataSource,
    private val localData: LocalDataSource
): IMovieRepository {

    override fun getAllMovies(): Flow<Resource<List<Movie>>> {
        return object: NetworkBoundResource<List<Movie>, List<MovieDetailResponse>>(){
            override fun loadFromDB(): Flow<List<Movie>> {
                return localData.getAllMovies().map {
                    DataMapper.mapMovieEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieDetailResponse>>> {
                return remoteData.getAllMovies()
            }

            override suspend fun saveCallResult(data: List<MovieDetailResponse>) {
                val movieList = DataMapper.mapMovieResponsesToEntities(data)
                localData.insertAllMovie(movieList)
            }

        }.asFlow()
    }

    override fun getAllTvShow(): Flow<Resource<List<TvShow>>> {
        return object: NetworkBoundResource<List<TvShow>, List<TvDetailResponse>>(){
            override fun loadFromDB(): Flow<List<TvShow>> {
                return localData.getAllTv().map {
                    DataMapper.mapTvEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<TvShow>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<TvDetailResponse>>> {
                return remoteData.getAllTvShows()
            }

            override suspend fun saveCallResult(data: List<TvDetailResponse>) {
                val movieList = DataMapper.mapTvResponsesToEntities(data)
                localData.insertAllTvShow(movieList)
            }

        }.asFlow()
    }

    override fun getMoviesBySearch(search: String): Flow<List<Movie>> {
        return localData.getMoviesBySearch(search).map {
            DataMapper.mapMovieEntitiesToDomain(it)
        }
    }

    override fun getTvsBySearch(search: String): Flow<List<TvShow>> {
        return localData.getTvsBySearch(search).map {
            DataMapper.mapTvEntitiesToDomain(it)
        }
    }


    //===========================================================
    //favorite
    override fun getAllMoviesFavorite(): Flow<List<Movie>> {
        return localData.getAllMoviesFavorite().map {
            DataMapper.mapMovieEntitiesToDomain(it)
        }
    }

    override fun getAllTvFavorite(): Flow<List<TvShow>> {
        return localData.getAllTvFavorite().map {
            DataMapper.mapTvEntitiesToDomain(it)
        }
    }

    override fun getMoviesFavBySearch(search: String): Flow<List<Movie>> {
        return localData.getMoviesFavBySearch(search).map {
            DataMapper.mapMovieEntitiesToDomain(it)
        }
    }

    override fun getTvsFavBySearch(search: String): Flow<List<TvShow>> {
        return localData.getTvsFavBySearch(search).map {
            DataMapper.mapTvEntitiesToDomain(it)
        }
    }

    override fun setMovieToFavorite(movie: Movie, newState: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            localData.setMovieToFav(
                DataMapper.mapMovieDomainToEntity(movie),
                newState
            )
        }
    }

    override fun setTvToFavorite(tvShow: TvShow, newState: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            localData.setTvToFav(
                DataMapper.mapTvDomainToEntity(tvShow),
                newState
            )
        }
    }

}