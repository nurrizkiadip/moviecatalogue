package com.nurrizkiadip_a1201541.moviecatalogue.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.data.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.data.Repository
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse

class MoviesCatalogueViewModel(
    private val repository: Repository
): ViewModel() {

    fun getMoviesData(): LiveData<ApiResponse<List<Movie>>> {
        return repository.getAllMovies()
    }

}
