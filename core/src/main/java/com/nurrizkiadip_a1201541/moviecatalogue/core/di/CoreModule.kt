package com.nurrizkiadip_a1201541.moviecatalogue.core.di

import androidx.room.Room
import com.nurrizkiadip_a1201541.moviecatalogue.core.BuildConfig.BASE_URL
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.MovieRepository
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.LocalDataSource
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.local.room.MovieDatabase
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.RemoteDataSource
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.source.remote.network.ApiService
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.repository.IMovieRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("moviecatalogue".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java,
            "movie.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    factory{ get<MovieDatabase>().movieDao() }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single {
        LocalDataSource(get())
    }
    single {
        RemoteDataSource(get())
    }

    single<IMovieRepository> {
        MovieRepository(get(), get())
    }
}