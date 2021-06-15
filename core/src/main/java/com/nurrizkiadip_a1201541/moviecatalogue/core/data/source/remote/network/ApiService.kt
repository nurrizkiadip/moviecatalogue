package com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.network

import com.nurrizkiadip_a1201541.moviecatalogue.core.BuildConfig.API_KEY
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.TvShowResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("3/movie/popular")
    suspend fun getAllMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("3/tv/popular")
    suspend fun getAllTvShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): TvShowResponse

}