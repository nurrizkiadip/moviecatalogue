package com.nurrizkiadip_a1201541.moviecatalogue.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.nurrizkiadip_a1201541.moviecatalogue.utils.EspressoIdlingResource
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.nurrizkiadip_a1201541.moviecatalogue.R
import com.nurrizkiadip_a1201541.moviecatalogue.ui.home.utils.*
import com.nurrizkiadip_a1201541.moviecatalogue.utils.OkHttpProvider
import com.nurrizkiadip_a1201541.moviecatalogue.utils.toEngLang
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest: DatabaseTest() {

    private val movieEntities = MoviesDataEntity.generateMovieData()
    private val tvShowEntities = MoviesDataEntity.generateTvShowsData()

    @get:Rule
    var activityRule = ActivityScenarioRule(HomeActivity::class.java)

    private lateinit var mockMovieResponseSuccess: MockResponse
    private lateinit var mockMovieResponseEmpty: MockResponse
    private lateinit var mockTvResponseSuccess: MockResponse
    private lateinit var mockTvResponseEmpty: MockResponse

    private lateinit var mockDetailMovieResponseSuccess: MockResponse
    private lateinit var mockDetailMovieResponseEmpty: MockResponse
    private lateinit var mockDetailTvResponseSuccess: MockResponse
    private lateinit var mockDetailTvResponseEmpty: MockResponse

    private val mockWebServer = MockWebServer()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext


    @Before
    fun setup(){
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                OkHttpProvider.getOkHttpClient()
            )
        )


        mockMovieResponseSuccess = context.createResponse("movie_popular.json", 200)
        mockMovieResponseEmpty = context.createResponse("movie_popular_empty.json", 200)
        mockTvResponseSuccess = context.createResponse("tv_popular.json", 200)
        mockTvResponseEmpty = context.createResponse("tv_popular_empty.json", 200)

        mockDetailMovieResponseSuccess = context.createResponse("mortal_kombat.json", 200)
        mockDetailMovieResponseEmpty = context.createResponse("mortal_kombat_empty.json", 200)
        mockDetailTvResponseSuccess = context.createResponse("the_falcon_and_the_winter_soldier.json", 200)
        mockDetailTvResponseEmpty = context.createResponse("the_falcon_and_the_winter_soldier_empty.json", 200)
    }

    @After
    @Throws(IOException::class)
    fun stop(){
        mockWebServer.shutdown()
        IdlingRegistry.getInstance()
            .unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun loadAllMoviesSuccess() {
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockMovieResponseSuccess
            }
        }

        R.id.navigation_movie.click()

        R.id.rv_movies.isDisplayed()
        onView(withId(R.id.rv_movies)).perform(
            RecyclerViewActions.scrollToPosition
            <RecyclerView.ViewHolder>(9)
        )
    }

    @Test
    fun loadAllTvShowsSuccess() {
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockTvResponseSuccess
            }
        }

        R.id.navigation_tv_show.click()

        R.id.rv_tvshow.isDisplayed()
        onView(withId(R.id.rv_tvshow)).perform(
            RecyclerViewActions.scrollToPosition
            <RecyclerView.ViewHolder>(9)
        )
    }

    @Test
    fun loadAllMoviesEmpty(){
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockMovieResponseEmpty
            }
        }
        R.id.navigation_movie.click()

        R.id.img_no_data.isDisplayed()
        R.id.tv_no_data.isDisplayed()
        R.id.tv_no_data.matchesWith("No Movies Data")
    }

    @Test
    fun loadAllTvShowsEmpty(){
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockTvResponseEmpty
            }
        }
        R.id.navigation_tv_show.click()

        R.id.img_no_data.isDisplayed()
        R.id.tv_no_data.isDisplayed()
        R.id.tv_no_data.matchesWith("No Tvs Data")
    }

    @Test
    fun loadAllMoviesError(){
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
            }
        }
        R.id.navigation_movie.click()


        R.id.img_no_data.isDisplayed()
        R.id.tv_no_data.isDisplayed()
        R.id.tv_no_data.matchesWith("Cannot getting data")
    }

    @Test
    fun loadAllTvsError(){
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
            }
        }
        R.id.navigation_tv_show.click()

        R.id.img_no_data.isDisplayed()
        R.id.tv_no_data.isDisplayed()
        R.id.tv_no_data.matchesWith("Cannot getting data")
    }

    @Test
    fun loadDetailMovieSuccess() {

        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockMovieResponseSuccess
            }
        }

        R.id.navigation_movie.click()
        R.id.rv_movies.isDisplayed()

        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockDetailMovieResponseSuccess
            }
        }

        onView(withId(R.id.rv_movies)).perform(
            RecyclerViewActions.actionOnItemAtPosition
            <RecyclerView.ViewHolder>(0, click())
        )

        R.id.tv_id_detail.isDisplayed()
        R.id.tv_id_detail.matchesWith(movieEntities[0].movieId)

        R.id.tv_title_detail.isDisplayed()
        R.id.tv_title_detail.matchesWith(movieEntities[0].title.toString())

        R.id.tv_language_detail.isDisplayed()
        R.id.tv_language_detail.matchesWith(movieEntities[0].originalLanguage?.toEngLang().toString())

        R.id.tv_release_detail.isDisplayed()
        R.id.tv_release_detail.matchesWith(movieEntities[0].releaseDate.toString())

        R.id.tv_popularity_detail.isDisplayed()
        R.id.tv_popularity_detail.matchesWith(movieEntities[0].popularity.toString())

        R.id.tv_overview_detail.isDisplayed()
        R.id.tv_overview_detail.matchesWith(movieEntities[0].overview.toString())
    }

    @Test
    fun loadDetailMovieEmpty(){
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockMovieResponseSuccess
            }
        }

        R.id.navigation_movie.click()
        R.id.rv_movies.isDisplayed()

        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockDetailMovieResponseEmpty
            }
        }
        onView(withId(R.id.rv_movies)).perform(
            RecyclerViewActions.actionOnItemAtPosition
            <RecyclerView.ViewHolder>(0, click())
        )


        R.id.img_no_data_detail.isDisplayed()
        R.id.tv_no_data_detail.isDisplayed()
        R.id.tv_no_data_detail.matchesWith("No Movie Data")
    }

    @Test
    fun loadDetailMovieError(){
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockMovieResponseSuccess
            }
        }

        R.id.navigation_movie.click()

        R.id.rv_movies.isDisplayed()
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
            }
        }

        onView(withId(R.id.rv_movies)).perform(
            RecyclerViewActions.actionOnItemAtPosition
            <RecyclerView.ViewHolder>(0, click())
        )

        R.id.img_no_data_detail.isDisplayed()
        R.id.tv_no_data_detail.isDisplayed()
        R.id.tv_no_data_detail.matchesWith("Cannot getting data")
    }

    @Test
    fun loadDetailTvSuccess() {
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockTvResponseSuccess
            }
        }
        R.id.navigation_tv_show.click()
        R.id.rv_tvshow.isDisplayed()

        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockDetailTvResponseSuccess
            }
        }

        onView(withId(R.id.rv_tvshow)).perform(
            RecyclerViewActions.actionOnItemAtPosition
            <RecyclerView.ViewHolder>(0, click())
        )

        R.id.tv_id_detail.isDisplayed()
        R.id.tv_id_detail.matchesWith(tvShowEntities[0].tvId)

        R.id.tv_title_detail.isDisplayed()
        R.id.tv_title_detail.matchesWith(tvShowEntities[0].title.toString())

        R.id.tv_language_detail.isDisplayed()
        R.id.tv_language_detail.matchesWith(tvShowEntities[0].originalLanguage?.toEngLang().toString())

        R.id.tv_release_detail.isDisplayed()
        R.id.tv_release_detail.matchesWith(tvShowEntities[0].firstAirDate.toString())

        R.id.tv_popularity_detail.isDisplayed()
        R.id.tv_popularity_detail.matchesWith(tvShowEntities[0].popularity.toString())

        R.id.tv_number_of_eps.isDisplayed()
        R.id.tv_number_of_eps.matchesWith(tvShowEntities[0].numberOfEpisodes.toString())

        R.id.tv_number_of_seasons.isDisplayed()
        R.id.tv_number_of_seasons.matchesWith(tvShowEntities[0].numberOfSeasons.toString())

        R.id.tv_overview_detail.isDisplayed()
        R.id.tv_overview_detail.matchesWith(tvShowEntities[0].overview.toString())

    }

    @Test
    fun loadDetailTvEmpty(){
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockTvResponseSuccess
            }
        }

        R.id.navigation_tv_show.click()
        R.id.rv_tvshow.isDisplayed()

        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockDetailTvResponseEmpty
            }
        }
        onView(withId(R.id.rv_tvshow)).perform(
            RecyclerViewActions.actionOnItemAtPosition
            <RecyclerView.ViewHolder>(0, click())
        )

        R.id.img_no_data_detail.isDisplayed()
        R.id.tv_no_data_detail.isDisplayed()
        R.id.tv_no_data_detail.matchesWith("No Tv Data")
    }

    @Test
    fun loadDetailTvError(){
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockTvResponseSuccess
            }
        }

        R.id.navigation_tv_show.click()
        R.id.rv_tvshow.isDisplayed()
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
            }
        }

        onView(withId(R.id.rv_tvshow)).perform(
            RecyclerViewActions.actionOnItemAtPosition
            <RecyclerView.ViewHolder>(0, click())
        )

        R.id.img_no_data_detail.isDisplayed()
        R.id.tv_no_data_detail.isDisplayed()
        R.id.tv_no_data_detail.matchesWith("Cannot getting data")
    }

    @Test
    fun addDataToFavorite() {

        val dummyMovie = movieEntities[0]
        appDatabase.movieDao().insertMovieFav(dummyMovie)

        val getDataMovie = appDatabase.movieDao().getAllMovieFavoriteLiveData()
        assertNotNull(getDataMovie)

        val dummyTv = tvShowEntities[0]
        appDatabase.movieDao().insertTvShowFav(dummyTv)

        val getDataTv = appDatabase.movieDao().getAllTvFavoriteLiveData()
        assertNotNull(getDataTv)
    }


}