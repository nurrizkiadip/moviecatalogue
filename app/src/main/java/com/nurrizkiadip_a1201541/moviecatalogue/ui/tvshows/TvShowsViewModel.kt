package com.nurrizkiadip_a1201541.moviecatalogue.ui.tvshows

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.data.Repository
import com.nurrizkiadip_a1201541.moviecatalogue.data.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse

class TvShowsViewModel(
    private val repository: Repository,
): ViewModel(){

    fun getAllTvShows(): LiveData<ApiResponse<List<TvShow>>> = repository.getAllTvShow()
}
