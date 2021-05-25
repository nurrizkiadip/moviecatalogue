package com.nurrizkiadip_a1201541.moviecatalogue.nav_ui.tvshows

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.Resource
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.usecase.MovieUseCase

class TvShowsViewModel constructor(
    private val movieUseCase: MovieUseCase
): ViewModel(){
    val allTvShows: LiveData<Resource<List<TvShow>>> = movieUseCase.getAllTvShow().asLiveData()

    fun getListTvsSearch(search: String): LiveData<List<TvShow>> {
        return movieUseCase.getTvsBySearch(search).asLiveData()
    }
}
