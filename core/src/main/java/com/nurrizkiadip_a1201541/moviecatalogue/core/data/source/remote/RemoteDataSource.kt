package com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote

import android.util.Log
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.network.ApiService
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.MovieDetailResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.nurrizkiadip_a1201541.moviecatalogue.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class RemoteDataSource(private val apiService: ApiService) {

    companion object{
        private val TAG: String = RemoteDataSource::class.java.simpleName
    }

    suspend fun getAllMovies(): Flow<ApiResponse<List<MovieDetailResponse>>> {
        return flow {
            try{
                val response = apiService.getAllMovies()
                val dataArray = response.results

                if(dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.d(TAG, "getAllMovies: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllTvShows(): Flow<ApiResponse<List<TvDetailResponse>>> {
        return flow {
            try{
                val response = apiService.getAllTvShows()
                val dataArray = DataMapper.mapTvResultItemToTvDetailResponse(response.results)

                if(dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.d(TAG, "getAllTvShows: $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}