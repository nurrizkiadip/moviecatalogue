package com.nurrizkiadip_a1201541.moviecatalogue.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nurrizkiadip_a1201541.moviecatalogue.data.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.data.Repository
import com.nurrizkiadip_a1201541.moviecatalogue.data.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.SuccessResponse
import com.nurrizkiadip_a1201541.moviecatalogue.utils.MoviesData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    @Mock
    private lateinit var observerMovie: Observer<ApiResponse<Movie>>
    @Mock
    private lateinit var observerTv: Observer<ApiResponse<TvShow>>

    private val dummyMovie = MoviesData.generateMovieData()[0]
    private val movieId = dummyMovie.movieId.toInt()
    private val dummyTvShow = MoviesData.generateTvShowsData()[0]
    private val tvId = dummyTvShow.tvId.toInt()


    @Before
    fun setUp() {
        viewModel = DetailViewModel(repository)
    }

    @Test
    fun getMovieDetail() {
        val movieState = MutableLiveData<ApiResponse<Movie>>()
        movieState.value = SuccessResponse(MoviesData.generateMovieData()[0])

        Mockito.`when`(repository.getMovieById(movieId)).thenReturn(movieState)

        viewModel.getMovieDetail(movieId.toString())
        val movieResponse = viewModel.movieDetail.value as SuccessResponse

        val movieEntity = movieResponse.body as Movie
        verify(repository).getMovieById(movieId)
        assertNotNull(movieEntity)

        assertEquals(dummyMovie.movieId, movieEntity.movieId)
        assertEquals(dummyMovie.posterPath, movieEntity.posterPath)
        assertEquals(dummyMovie.title, movieEntity.title)
        assertEquals(dummyMovie.overview, movieEntity.overview)
        assertEquals(dummyMovie.popularity, movieEntity.popularity)
        assertEquals(dummyMovie.releaseDate, movieEntity.releaseDate)
        assertEquals(dummyMovie.originalLanguage, movieEntity.originalLanguage)

        viewModel.movieDetail.observeForever(observerMovie)
        verify(observerMovie).onChanged(movieState.value)
    }

    @Test
    fun getTvShowDetail() {
        val tvState = MutableLiveData<ApiResponse<TvShow>>()
        tvState.value = SuccessResponse(MoviesData.generateTvShowsData()[0])

        Mockito.`when`(repository.getTvById(tvId)).thenReturn(tvState)
        viewModel.getTvShowDetail(tvId.toString())
        val tvResponse = viewModel.tvShowDetail.value as SuccessResponse
        val tvEntity = tvResponse.body as TvShow
        assertNotNull(tvEntity)

        assertEquals(dummyTvShow.tvId, tvEntity.tvId)
        assertEquals(dummyTvShow.posterPath, tvEntity.posterPath)
        assertEquals(dummyTvShow.title, tvEntity.title)
        assertEquals(dummyTvShow.overview, tvEntity.overview)
        assertEquals(dummyTvShow.popularity, tvEntity.popularity)
        assertEquals(dummyTvShow.firstAirDate, tvEntity.firstAirDate)
        assertEquals(dummyTvShow.numberOfEpisodes, tvEntity.numberOfEpisodes)
        assertEquals(dummyTvShow.numberOfSeasons, tvEntity.numberOfSeasons)
        assertEquals(dummyTvShow.originalLanguage, tvEntity.originalLanguage)

        viewModel.tvShowDetail.observeForever(observerTv)
        verify(observerTv).onChanged(tvState.value)
    }

    @Test
    fun setMovieToFavorite(){

        doNothing().`when`(repository).setMovieToFavorite(dummyMovie)
        viewModel.setMovieFavorite(dummyMovie)

        verify(repository, times(1)).setMovieToFavorite(dummyMovie)
    }

    @Test
    fun setTvToFavorite(){
        doNothing().`when`(repository).setTvToFavorite(dummyTvShow)
        viewModel.setTvFavorite(dummyTvShow)
        verify(repository, times(1)).setTvToFavorite(dummyTvShow)
    }

    @Test
    fun deleteMovieFavorite(){
        doNothing().`when`(repository).setMovieToFavorite(dummyMovie)

        viewModel.setMovieFavorite(dummyMovie)
        verify(repository, times(1)).setMovieToFavorite(dummyMovie)
        viewModel.deleteMovieFav(dummyMovie)
        verify(repository, times(1)).deleteMovieFav(dummyMovie)
    }

    @Test
    fun deleteTvFavorite(){
        doNothing().`when`(repository).setTvToFavorite(dummyTvShow)

        viewModel.setTvFavorite(dummyTvShow)
        verify(repository, times(1)).setTvToFavorite(dummyTvShow)
        viewModel.deleteTvFav(dummyTvShow)
        verify(repository, times(1)).deleteTvShowFav(dummyTvShow)
    }
}