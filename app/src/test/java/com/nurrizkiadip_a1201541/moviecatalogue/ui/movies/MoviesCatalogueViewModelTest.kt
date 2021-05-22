package com.nurrizkiadip_a1201541.moviecatalogue.ui.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.nurrizkiadip_a1201541.moviecatalogue.data.Movie
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.nurrizkiadip_a1201541.moviecatalogue.data.Repository
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.SuccessResponse
import com.nurrizkiadip_a1201541.moviecatalogue.utils.MoviesData

@RunWith(MockitoJUnitRunner.Silent::class)
class MoviesCatalogueViewModelTest {
    private lateinit var viewModel: MoviesCatalogueViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    @Mock
    private lateinit var moviesObserver: Observer<ApiResponse<List<Movie>>>

    private val dummyMovies = MoviesData.generateMovieData()

    @Before
    fun setUp() {
        viewModel = MoviesCatalogueViewModel(repository)
    }

    @Test
    fun `getMoviesData should be success`() {
        val movieState = MutableLiveData<ApiResponse<List<Movie>>>()
        movieState.value = SuccessResponse(dummyMovies)

        Mockito.`when`(repository.getAllMovies()).thenReturn(movieState)

        val moviesEntities = viewModel.getMoviesData().value as SuccessResponse
        verify(repository).getAllMovies()
        assertNotNull(moviesEntities)
        assertNotNull(moviesEntities.body)
        assertEquals(10, moviesEntities.body?.size)

        viewModel.getMoviesData().observeForever(moviesObserver)
        verify(moviesObserver).onChanged(movieState.value)
    }

    @Test
    fun `getMoviesData should be success but data is empty`() {
        val movieState = MutableLiveData<ApiResponse<List<Movie>>>()
        movieState.value = SuccessResponse(listOf())

        Mockito.`when`(repository.getAllMovies()).thenReturn(movieState)

        val moviesEntities = viewModel.getMoviesData().value as SuccessResponse
        verify(repository).getAllMovies()
        assertTrue("size of data should be 0, actual is ${moviesEntities.body?.size}", moviesEntities.body?.size == 0)

        viewModel.getMoviesData().observeForever(moviesObserver)
        verify(moviesObserver).onChanged(movieState.value)
    }

}