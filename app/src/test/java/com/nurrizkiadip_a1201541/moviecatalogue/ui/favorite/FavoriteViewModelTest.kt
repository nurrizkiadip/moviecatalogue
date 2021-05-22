package com.nurrizkiadip_a1201541.moviecatalogue.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.nhaarman.mockitokotlin2.verify
import com.nurrizkiadip_a1201541.moviecatalogue.data.Repository
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.MovieEntity
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.utils.MoviesDataEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executors

@RunWith(MockitoJUnitRunner.Silent::class)
class FavoriteViewModelTest {

    private lateinit var viewModel: FavoriteViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var movieObserver: Observer<PagedList<MovieEntity>>
    @Mock
    private lateinit var tvShowObserver: Observer<PagedList<TvShowEntity>>

    private val dummyMovies = MoviesDataEntity.generateMovieData()
    private val dummyTvShows = MoviesDataEntity.generateTvShowsData()

    @Before
    fun setUp() {
        viewModel = FavoriteViewModel(repository)
    }

    @Test
    fun `getAllMovieFavorite should be success`() {
        val movieState = MutableLiveData<PagedList<MovieEntity>>()
        movieState.value = PagedTestDataSources.snapshot(dummyMovies)

        Mockito.`when`(repository.getAllMoviesFavorite()).thenReturn(movieState)

        viewModel.getAllMovieFavorite().observeForever(movieObserver)
        verify(movieObserver).onChanged(movieState.value)

        val expectedValue = movieState.value
        val moviesEntities = viewModel.getAllMovieFavorite().value
        assertNotNull(moviesEntities)
        assertEquals(expectedValue, moviesEntities)
        assertEquals(expectedValue?.snapshot(), moviesEntities?.snapshot())
        assertEquals(expectedValue?.size, moviesEntities?.size)
    }

    @Test
    fun `getAllMovieFavorite should be success but data is empty`() {
        val movieState = MutableLiveData<PagedList<MovieEntity>>()
        movieState.value = PagedTestDataSources.snapshot()

        Mockito.`when`(repository.getAllMoviesFavorite()).thenReturn(movieState)

        viewModel.getAllMovieFavorite().observeForever(movieObserver)
        verify(movieObserver).onChanged(movieState.value)

        val moviesEntities = viewModel.getAllMovieFavorite().value?.size
        assertTrue("size of data should be 0, actual is $moviesEntities", moviesEntities == 0)
    }

    @Test
    fun `getAllTvFavorite should be success`() {
        val tvState = MutableLiveData<PagedList<TvShowEntity>>()
        tvState.value = PagedTestDataSources.snapshot(dummyTvShows)

        Mockito.`when`(repository.getAllTvFavorite()).thenReturn(tvState)

        viewModel.getAllTvFavorite().observeForever(tvShowObserver)
        verify(tvShowObserver).onChanged(tvState.value)

        val expectedValue = tvState.value
        val tvsEntities = viewModel.getAllTvFavorite().value
        assertNotNull(tvsEntities)
        assertEquals(expectedValue, tvsEntities)
        assertEquals(expectedValue?.snapshot(), tvsEntities?.snapshot())
        assertEquals(expectedValue?.size, tvsEntities?.size)
    }

    @Test
    fun `getAllTvFavorite should be success but data is empty`() {
        val tvState = MutableLiveData<PagedList<TvShowEntity>>()
        tvState.value = PagedTestDataSources.snapshot()

        Mockito.`when`(repository.getAllTvFavorite()).thenReturn(tvState)

        viewModel.getAllTvFavorite().observeForever(tvShowObserver)
        verify(tvShowObserver).onChanged(tvState.value)

        val tvsEntities = viewModel.getAllTvFavorite().value?.size
        assertTrue("size of data should be 0, actual is $tvsEntities", tvsEntities == 0)
    }

    @Test
    fun `getMoviesBySearch should be success`() {
        val dummyQuery = ""
        val tvState = MutableLiveData<PagedList<MovieEntity>>()
        tvState.value = PagedTestDataSources.snapshot(dummyMovies)

        Mockito.`when`(repository.getMoviesFavBySearch(dummyQuery)).thenReturn(tvState)

        viewModel.getMoviesBySearch(dummyQuery).observeForever(movieObserver)
        verify(movieObserver).onChanged(tvState.value)

        val expectedValue = tvState.value
        val moviesEntities = viewModel.getMoviesBySearch(dummyQuery).value
        assertNotNull(moviesEntities)
        assertEquals(expectedValue, moviesEntities)
        assertEquals(expectedValue?.snapshot(), moviesEntities?.snapshot())
        assertEquals(expectedValue?.size, moviesEntities?.size)
    }

    @Test
    fun `getMoviesBySearch should be success but data is empty`() {
        val dummyQuery = ""
        val tvState = MutableLiveData<PagedList<MovieEntity>>()
        tvState.value = PagedTestDataSources.snapshot()

        Mockito.`when`(repository.getMoviesFavBySearch(dummyQuery)).thenReturn(tvState)

        viewModel.getMoviesBySearch(dummyQuery).observeForever(movieObserver)
        verify(movieObserver).onChanged(tvState.value)

        val moviesEntities = viewModel.getMoviesBySearch(dummyQuery).value?.size
        assertTrue("size of data should be 0, actual is $moviesEntities", moviesEntities == 0)
    }


    @Test
    fun `getTvsBySearch should be success`() {
        val dummyQuery = ""
        val tvState = MutableLiveData<PagedList<TvShowEntity>>()
        tvState.value = PagedTestDataSources.snapshot(dummyTvShows)

        Mockito.`when`(repository.getTvsFavBySearch(dummyQuery)).thenReturn(tvState)

        viewModel.getTvsBySearch(dummyQuery).observeForever(tvShowObserver)
        verify(tvShowObserver).onChanged(tvState.value)

        val expectedValue = tvState.value
        val tvsEntities = viewModel.getTvsBySearch(dummyQuery).value
        assertNotNull(tvsEntities)
        assertEquals(expectedValue, tvsEntities)
        assertEquals(expectedValue?.snapshot(), tvsEntities?.snapshot())
        assertEquals(expectedValue?.size, tvsEntities?.size)
    }

    @Test
    fun `getTvsBySearch should be success but data is empty`() {
        val dummyQuery = ""
        val tvState = MutableLiveData<PagedList<TvShowEntity>>()
        tvState.value = PagedTestDataSources.snapshot()

        Mockito.`when`(repository.getTvsFavBySearch(dummyQuery)).thenReturn(tvState)

        viewModel.getTvsBySearch(dummyQuery).observeForever(tvShowObserver)
        verify(tvShowObserver).onChanged(tvState.value)

        val tvsEntities = viewModel.getTvsBySearch(dummyQuery).value?.size
        assertTrue("size of data should be 0, actual is $tvsEntities", tvsEntities == 0)

    }

    class PagedTestDataSources <T> private constructor(
        private val items: List<T>
    ) : PositionalDataSource<T>() {
        companion object {
            fun <T> snapshot(items: List<T> = listOf()): PagedList<T> {
                return PagedList.Builder(PagedTestDataSources(items), 10)
                    .setNotifyExecutor(Executors.newSingleThreadExecutor())
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .build()
            }
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
            callback.onResult(items, 0, items.size)
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
            val start = params.startPosition
            val end = params.startPosition + params.loadSize
            callback.onResult(items.subList(start, end))
        }
    }
}