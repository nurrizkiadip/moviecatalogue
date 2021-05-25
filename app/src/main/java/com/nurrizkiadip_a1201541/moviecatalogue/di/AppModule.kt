package com.nurrizkiadip_a1201541.moviecatalogue.di

import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.usecase.MovieInteractor
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.usecase.MovieUseCase
import com.nurrizkiadip_a1201541.moviecatalogue.detail.DetailViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.nav_ui.movies.MovieViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.nav_ui.tvshows.TvShowsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}

val viewModelModule = module {
    viewModel {
        DetailViewModel(get())
    }
    viewModel {
        MovieViewModel(get())
    }
    viewModel {
        TvShowsViewModel(get())
    }
}