package com.nurrizkiadip_a1201541.moviecatalogue.favorite.di

import com.nurrizkiadip_a1201541.moviecatalogue.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    viewModel{
        FavoriteViewModel(get())
    }
}