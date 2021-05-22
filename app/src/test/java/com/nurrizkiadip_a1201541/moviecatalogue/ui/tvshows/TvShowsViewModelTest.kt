package com.nurrizkiadip_a1201541.moviecatalogue.ui.tvshows

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nurrizkiadip_a1201541.moviecatalogue.data.Repository
import com.nurrizkiadip_a1201541.moviecatalogue.data.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ApiResponse
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.SuccessResponse
import com.nurrizkiadip_a1201541.moviecatalogue.utils.MoviesData
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class TvShowsViewModelTest {

    private lateinit var viewModel: TvShowsViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var tvShowObserver: Observer<ApiResponse<List<TvShow>>>

    private val dummyTvShows = MoviesData.generateTvShowsData()

    @Before
    fun setUp() {
        viewModel = TvShowsViewModel(repository)
    }

    @Test
    fun `getTvShowsData should be success`() {
        val tvState = MutableLiveData<ApiResponse<List<TvShow>>>()
        tvState.value = SuccessResponse(dummyTvShows)

        Mockito.`when`(repository.getAllTvShow()).thenReturn(tvState)

        val tvShowEntities = viewModel.getAllTvShows().value as SuccessResponse
        verify(repository).getAllTvShow()
        assertNotNull(tvShowEntities)
        assertNotNull(tvShowEntities.body)
        assertEquals(10, tvShowEntities.body?.size)

        viewModel.getAllTvShows().observeForever(tvShowObserver)
        verify(tvShowObserver).onChanged(tvState.value)
    }

    @Test
    fun `getTvShowsData should be success but data is empty`() {
        val tvState = MutableLiveData<ApiResponse<List<TvShow>>>()
        tvState.value = SuccessResponse(listOf())

        Mockito.`when`(repository.getAllTvShow()).thenReturn(tvState)

        val tvShowEntities = viewModel.getAllTvShows().value as SuccessResponse
        verify(repository).getAllTvShow()
        assertTrue(
            "size of data should be 0, actual is ${tvShowEntities.body?.size}",
            tvShowEntities.body?.size == 0
        )

        viewModel.getAllTvShows().observeForever(tvShowObserver)
        verify(tvShowObserver).onChanged(tvState.value)
    }

}