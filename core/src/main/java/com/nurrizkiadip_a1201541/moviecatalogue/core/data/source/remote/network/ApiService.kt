package com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.network

import com.nurrizkiadip_a1201541.moviecatalogue.core.BuildConfig.API_KEY
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.MovieDetailResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.TvShowResponse
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("3/movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = API_KEY,
    ): MovieDetailResponse

    @GET("3/tv/{id}")
    suspend fun getTvById(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = API_KEY,
    ): TvDetailResponse

}