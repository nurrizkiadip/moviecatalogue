package com.nurrizkiadip_a1201541.moviecatalogue.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nhaarman.mockitokotlin2.*
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.LocalDataSource
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.RemoteDataSource
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.SuccessResponse
import com.nurrizkiadip_a1201541.moviecatalogue.utils.LiveDataTestUtil
import com.nurrizkiadip_a1201541.moviecatalogue.utils.MoviesData
import com.nurrizkiadip_a1201541.moviecatalogue.utils.PagedListUtil
import com.nurrizkiadip_a1201541.moviecatalogue.vo.SuccessResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class RepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remoteData: RemoteDataSource
    @Mock
    private lateinit var localData: LocalDataSource

    private lateinit var fakeRepository: FakeRepository

    private val moviesResponse = MoviesData.generateMovieData()
    private val movieId = moviesResponse[0].movieId.toInt()
    private val tvResponse = MoviesData.generateTvShowsData()
    private val tvId = tvResponse[0].tvId.toInt()

    @Before
    fun setUp() {
        fakeRepository = FakeRepository(remoteData, localData, GlobalScope)

    }

    @Test
    fun getAllMovies() {
        val movies = MutableLiveData<ApiResponse<List<Movie>>>()
        movies.value = SuccessResponse(moviesResponse)

        doAnswer {
            (it.arguments[0] as RemoteDataSource.LoadAllMoviesCallback).onAllMoviesReceived(movies)
            null
        }.`when`(remoteData).getAllMovies(any())

        val movieEntities = LiveDataTestUtil.getValue(fakeRepository.getAllMovies()) as SuccessResponse
        verify(remoteData).getAllMovies(any())
        assertNotNull(movieEntities)
        assertEquals(moviesResponse.size, (movieEntities).body?.size)
    }

    @Test
    fun getAllTvShow() {
        val dummyTvLiveData = MutableLiveData<ApiResponse<List<TvShow>>>()
        dummyTvLiveData.value = SuccessResponse(tvResponse)

        Mockito.doAnswer {
            (it.arguments[0] as RemoteDataSource.LoadAllTvsCallback).onAllTvsReceived(dummyTvLiveData)
            null
        }.`when`(remoteData).getAllTvShows(any())

        val tvEntities = LiveDataTestUtil.getValue(fakeRepository.getAllTvShow()) as SuccessResponse
        verify(remoteData).getAllTvShows(any())
        assertNotNull(tvEntities)
        assertEquals(tvResponse.size, tvEntities.body?.size)
    }

    @Test
    fun getMovieById() {
        val dummyMovieLiveData = MutableLiveData<ApiResponse<Movie>>()
        dummyMovieLiveData.value = SuccessResponse(moviesResponse[0])

        Mockito.doAnswer {
            (it.arguments[1] as RemoteDataSource.LoadMovieByIdCallback).onMovieByIdReceived(dummyMovieLiveData)
            null
        }.`when`(remoteData).getMovieById(eq(movieId), any())

        val movieEntity = LiveDataTestUtil.getValue(fakeRepository.getMovieById(movieId)) as SuccessResponse
        verify(remoteData).getMovieById(eq(movieId), any())
        assertNotNull(movieEntity)
        assertEquals(moviesResponse[0].movieId, movieEntity.body?.movieId)

    }

    @Test
    fun getTvById() {
        val dummyTvLiveData = MutableLiveData<ApiResponse<TvShow>>()
        dummyTvLiveData.value = SuccessResponse(tvResponse[0])

        Mockito.doAnswer {
            (it.arguments[1] as RemoteDataSource.LoadTvByIdCallback).onTvByIdReceived(dummyTvLiveData)
            null
        }.`when`(remoteData).getTvById(ArgumentMatchers.eq(tvId), any())

        val tvEntity = LiveDataTestUtil.getValue(fakeRepository.getTvById(tvId)) as SuccessResponse
        verify(remoteData).getTvById(ArgumentMatchers.eq(tvId), any())
        assertNotNull(tvEntity)
        assertEquals(tvResponse[0].tvId, tvEntity.body?.tvId)
    }

    @Test
    fun getAllMoviesFavorite(){
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>

        `when`(localData.getAllMoviesFavorite()).thenReturn(dataSourceFactory)
        fakeRepository.getAllMoviesFavorite()

        val movieEntitiesFav = PagedListUtil.mockPagedList(MoviesData.generateMovieData())
        verify(localData).getAllMoviesFavorite()
        assertNotNull(movieEntitiesFav)
        assertEquals(moviesResponse.size.toLong(), movieEntitiesFav.size.toLong())
    }

    @Test
    fun getAllTvFavorite(){
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>

        `when`(localData.getAllTvFavorite()).thenReturn(dataSourceFactory)
        fakeRepository.getAllTvFavorite()

        val tvEntitiesFav = PagedListUtil.mockPagedList(MoviesData.generateTvShowsData())
        verify(localData).getAllTvFavorite()
        assertNotNull(tvEntitiesFav)
        assertEquals(tvResponse.size.toLong(), tvEntitiesFav.size.toLong())
    }

    @Test
    fun getMoviesFavBySearch(){
        val dummySearch = ""
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>

        `when`(localData.getMoviesFavBySearch(dummySearch)).thenReturn(dataSourceFactory)
        fakeRepository.getMoviesFavBySearch(dummySearch)

        val movieEntitiesFav = SuccessResource(PagedListUtil.mockPagedList(MoviesData.generateMovieData()))
        verify(localData).getMoviesFavBySearch(dummySearch)
        assertNotNull(movieEntitiesFav)
        assertEquals(moviesResponse.size.toLong(), movieEntitiesFav.data?.size?.toLong())
    }

    @Test
    fun getTvsFavBySearch(){
        val dummySearch = ""
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>

        `when`(localData.getTvsFavBySearch(dummySearch)).thenReturn(dataSourceFactory)
        fakeRepository.getTvsFavBySearch(dummySearch)

        val tvEntitiesFav = SuccessResource(PagedListUtil.mockPagedList(MoviesData.generateTvShowsData()))
        verify(localData).getTvsFavBySearch(dummySearch)
        assertNotNull(tvEntitiesFav)
        assertEquals(tvResponse.size.toLong(), tvEntitiesFav.data?.size?.toLong())
    }

    @Test
    fun setMovieToFavorite(){
        val movieDummy = moviesResponse[0]
        val movieEntity = MovieEntity(
            movieDummy.movieId,
            movieDummy.posterPath,
            movieDummy.title,
            movieDummy.overview,
            movieDummy.popularity,
            movieDummy.releaseDate,
            movieDummy.originalLanguage,
            movieDummy.isFavorite
        )
        doNothing().`when`(localData).setMovieFav(movieEntity)
        fakeRepository.setMovieToFavorite(movieDummy)
        verify(localData).setMovieFav(movieEntity)
    }

    @Test
    fun setTvToFavorite(){
        val tvShowDummy = tvResponse[0]
        val tvShowEntity = TvShowEntity(
            tvShowDummy.tvId,
            tvShowDummy.posterPath,
            tvShowDummy.title,
            tvShowDummy.overview,
            tvShowDummy.popularity,
            tvShowDummy.firstAirDate,
            tvShowDummy.numberOfEpisodes,
            tvShowDummy.numberOfSeasons,
            tvShowDummy.originalLanguage,
            tvShowDummy.isFavorite
        )

        doNothing().`when`(localData).setTvFav(tvShowEntity)
        fakeRepository.setTvToFavorite(tvShowDummy)
        verify(localData, times(1)).setTvFav(tvShowEntity)
    }

    @Test
    fun deleteMovieFavorite(){
        val movieDummy = moviesResponse[0]
        val movieEntity = MovieEntity(
            movieDummy.movieId,
            movieDummy.posterPath,
            movieDummy.title,
            movieDummy.overview,
            movieDummy.popularity,
            movieDummy.releaseDate,
            movieDummy.originalLanguage,
            movieDummy.isFavorite
        )

        doNothing().`when`(localData).setMovieFav(movieEntity)
        fakeRepository.setMovieToFavorite(movieDummy)
        verify(localData, times(1)).setMovieFav(movieEntity)

        doNothing().`when`(localData).deleteMovieFav(movieEntity)
        fakeRepository.deleteMovieFav(movieDummy)
        verify(localData).deleteMovieFav(movieEntity)
    }

    @Test
    fun deleteTvFavorite(){
        val tvShowDummy = tvResponse[0]
        val tvShowEntity = TvShowEntity(
            tvShowDummy.tvId,
            tvShowDummy.posterPath,
            tvShowDummy.title,
            tvShowDummy.overview,
            tvShowDummy.popularity,
            tvShowDummy.firstAirDate,
            tvShowDummy.numberOfEpisodes,
            tvShowDummy.numberOfSeasons,
            tvShowDummy.originalLanguage,
            tvShowDummy.isFavorite
        )

        doNothing().`when`(localData).setTvFav(tvShowEntity)
        fakeRepository.setTvToFavorite(tvShowDummy)
        verify(localData, times(1)).setTvFav(tvShowEntity)

        doNothing().`when`(localData).deleteTvFav(tvShowEntity)
        fakeRepository.deleteTvShowFav(tvShowDummy)
        verify(localData).deleteTvFav(tvShowEntity)
    }
}