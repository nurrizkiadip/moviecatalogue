package com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.nurrizkiadip_a1201541.moviecatalogue.data.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.data.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.utils.EspressoIdlingResource
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.network.ApiConfig
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.network.ApiService
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource(private val apiService: ApiService) {

    companion object{
        private val TAG: String = RemoteDataSource::class.java.simpleName

        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(application: Application):RemoteDataSource{
            return instance ?: synchronized(this){
                instance ?: RemoteDataSource(ApiConfig.getApiService(application)).apply { instance = this }
            }
        }
    }

    fun getAllMovies(callback: LoadAllMoviesCallback){
        val movieList = MutableLiveData<ApiResponse<List<Movie>>>()
        movieList.postValue(LoadingResponse())

        EspressoIdlingResource.increment()
        apiService.getMovies().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val responseAllMovie = response.body()?.results

                    if (responseAllMovie != null && responseAllMovie.isNotEmpty()) {
                        val list = ArrayList<Movie>()
                        for (movie in responseAllMovie) {
                            list.add(Movie(
                                movie.id.toString(),
                                movie.posterPath,
                                movie.title,
                                releaseDate = movie.releaseDate,
                            ))
                        }
                        Log.d(TAG, "onResponse: pada repos isi list movie ${responseAllMovie[0]}")
                        movieList.postValue(SuccessResponse(list))
                    } else {
                        Log.d(TAG, "onResponse: Unsuccessfull ${response.message()}")
                        movieList.postValue(EmptyResponse(listOf(),"No Movies Data"))
                    }
                } else {
                    movieList.postValue(EmptyResponse(listOf(),"No Movies Data"))
                }
            }
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                movieList.postValue(ErrorResponse("Cannot getting data"))
            }
        })

        callback.onAllMoviesReceived(movieList)
        EspressoIdlingResource.decrement()
    }

    fun getAllTvShows(callback: LoadAllTvsCallback){
        val tvList = MutableLiveData<ApiResponse<List<TvShow>>>()
        tvList.postValue(LoadingResponse())

        EspressoIdlingResource.increment()
        apiService.getTvShows().enqueue(object : Callback<TvShowResponse> {
            override fun onResponse(call: Call<TvShowResponse>, response: Response<TvShowResponse>) {
                if (response.isSuccessful) {
                    val responseAllTv = response.body()?.results

                    if (responseAllTv != null && responseAllTv.isNotEmpty()) {
                        val list = ArrayList<TvShow>()
                        for (tv in responseAllTv) {
                            list.add(TvShow(
                                tv.id.toString(),
                                tv.posterPath,
                                tv.name,
                                firstAirDate = tv.firstAirDate,
                            ))
                        }
                        tvList.postValue(SuccessResponse(list))
                    } else {
                        tvList.postValue(EmptyResponse(listOf(),"No Tvs Data"))
                    }
                } else {
                    Log.d(TAG, "onResponse: Unsuccessfull ${response.message()}")
                    tvList.postValue(EmptyResponse(listOf(),"No Tvs Data"))
                }
            }
            override fun onFailure(call: Call<TvShowResponse>, t: Throwable) {
                tvList.postValue(ErrorResponse("Cannot getting data"))
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })

        callback.onAllTvsReceived(tvList)
        EspressoIdlingResource.decrement()
    }

    fun getMovieById(id: Int, callback: LoadMovieByIdCallback){
        val movie = MutableLiveData<ApiResponse<Movie>>()
        movie.postValue(LoadingResponse())

        EspressoIdlingResource.increment()
        apiService.getMovieById(id = id).enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(
                call: Call<MovieDetailResponse>,
                response: Response<MovieDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseMovie = response.body()
                    if(responseMovie != null && responseMovie.overview != ""){
                        movie.postValue(SuccessResponse(
                            Movie(
                                responseMovie.id.toString(),
                                responseMovie.posterPath,
                                responseMovie.title,
                                responseMovie.overview,
                                responseMovie.popularity.toString(),
                                responseMovie.releaseDate,
                                responseMovie.originalLanguage
                            )
                        ))
//                        movie.postValue(SuccessResponse(responseMovie))
                    } else movie.postValue(EmptyResponse(null,"No Movie Data"))

                } else {
                    Log.d(TAG, "onResponse: Unsuccessfull ${response.message()}")
                    movie.postValue(EmptyResponse(null,"No Movie Data"))
                }
            }

            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                movie.postValue(ErrorResponse("Cannot getting data"))
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })

        callback.onMovieByIdReceived(movie)
        EspressoIdlingResource.decrement()
    }

    fun getTvById(id: Int, callback: LoadTvByIdCallback){
        val tv = MutableLiveData<ApiResponse<TvShow>>()
        tv.postValue(LoadingResponse())

        EspressoIdlingResource.increment()
        apiService.getTvById(id = id).enqueue(object : Callback<TvDetailResponse> {
            override fun onResponse(
                call: Call<TvDetailResponse>,
                response: Response<TvDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseTv = response.body()
                    if(responseTv != null && responseTv.overview != ""){
                        tv.postValue(SuccessResponse(
                            TvShow(
                                responseTv.id.toString(),
                                responseTv.posterPath,
                                responseTv.name,
                                responseTv.overview,
                                responseTv.popularity.toString(),
                                responseTv.firstAirDate,
                                responseTv.numberOfEpisodes,
                                responseTv.numberOfSeasons,
                                responseTv.originalLanguage
                            )
                        ))
//                        tv.postValue(SuccessResponse(responseTv))
                    } else tv.postValue(EmptyResponse(null,"No Tv Data"))

                } else {
                    Log.d(TAG, "onResponse: Unsuccessfull ${response.message()}")
                    tv.postValue(EmptyResponse(null, "No Tv Data"))
                }
            }
            override fun onFailure(call: Call<TvDetailResponse>, t: Throwable) {
                tv.postValue(ErrorResponse("Cannot getting data"))
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
        callback.onTvByIdReceived(tv)
        EspressoIdlingResource.decrement()
    }

    interface LoadAllMoviesCallback{
        fun onAllMoviesReceived(movieResponse: MutableLiveData<ApiResponse<List<Movie>>>)
    }
    interface LoadAllTvsCallback{
        fun onAllTvsReceived(tvResponse: MutableLiveData<ApiResponse<List<TvShow>>>)
    }
    interface LoadMovieByIdCallback{
        fun onMovieByIdReceived(movieResponse: MutableLiveData<ApiResponse<Movie>>)
    }
    interface LoadTvByIdCallback{
        fun onTvByIdReceived(tvResponse: MutableLiveData<ApiResponse<TvShow>>)
    }

}