package com.nurrizkiadip_a1201541.moviecatalogue

import android.app.Application
import com.nurrizkiadip_a1201541.moviecatalogue.core.di.databaseModule
import com.nurrizkiadip_a1201541.moviecatalogue.core.di.networkModule
import com.nurrizkiadip_a1201541.moviecatalogue.core.di.repositoryModule
import com.nurrizkiadip_a1201541.moviecatalogue.di.useCaseModule
import com.nurrizkiadip_a1201541.moviecatalogue.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MovieCatalogueApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MovieCatalogueApp)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )

        }
    }
}