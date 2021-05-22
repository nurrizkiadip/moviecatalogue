package com.nurrizkiadip_a1201541.moviecatalogue.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.nurrizkiadip_a1201541.moviecatalogue.BuildConfig.BASE_URL_MOVIEDB_IMAGE
import com.nurrizkiadip_a1201541.moviecatalogue.R
import com.nurrizkiadip_a1201541.moviecatalogue.data.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.data.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.*
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.ActivityDetailBinding
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.ContentDetailBinding
import com.nurrizkiadip_a1201541.moviecatalogue.viewmodel.ViewModelFactory
import com.nurrizkiadip_a1201541.moviecatalogue.utils.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var bindingContentDetail: ContentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var menu: Menu? = null
    private lateinit var type: String
    private var isFav: Boolean = false

    private lateinit var movieData: Movie
    private lateinit var tvData: TvShow

    companion object {
        private val TAG: String = DetailActivity::class.java.simpleName
        const val MOVIE_TYPE = "movie_type"
        const val TV_TYPE = "tv_type"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_DETAIL_TYPE = "extra_detail_type"
        const val EXTRA_DETAIL_IS_FAV = "extra_detail_is_fav"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        bindingContentDetail = binding.detailContent
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            elevation = 4f
        }

        val id = intent.getStringExtra(EXTRA_ID)
        Log.d(TAG, "onCreate: id: $id")
        type = intent.getStringExtra(EXTRA_DETAIL_TYPE) ?: ""
        Log.d(TAG, "onCreate: type: $type")
        isFav = intent.getBooleanExtra(EXTRA_DETAIL_IS_FAV, false)

        val factory = ViewModelFactory.getInstance(this, application, lifecycleScope)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]


        binding.progressBar.visible()
        if(type == MOVIE_TYPE){
            if(id != null){
                Log.d(TAG, "onCreate: tipe movie sudah sampai sini")
                viewModel.setId(id)


                if(!isFav) {
                    viewModel.movieFavDetail.observe(this){ isFav = it != null }
                    viewModel.getMovieDetail(id)
                    viewModel.movieDetail.observe(this) {
                        if (it != null) {
                            setMovieDataDetail(it)
                        }
                    }
                }
                else {
                    viewModel.movieFavDetail.observe(this) {
                        if (it != null) {

                            populateMovieDataContentDetail(Movie(
                                it.movieId,
                                it.posterPath,
                                it.title,
                                it.overview,
                                it.popularity,
                                it.releaseDate,
                                it.originalLanguage,
                                it.isFavorite
                            ))
                        }
                    }
                }
            }
        }
        else if(type == TV_TYPE){
            if(id != null){
                Log.d(TAG, "onCreate: tipe tv sudah sampai sini")
                viewModel.setId(id)

                if(!isFav) {
                    viewModel.tvShowFavDetail.observe(this){ isFav = it != null }

                    viewModel.getTvShowDetail(id)
                    viewModel.tvShowDetail.observe(this) {
                        if (it != null) {
                            setTvDataDetail(it)
                        }
                    }
                }

                else {
                    viewModel.tvShowFavDetail.observe(this) {
                        if (it != null) {
                            populateTvShowDataContentDetail(TvShow(
                                it.tvId,
                                it.posterPath,
                                it.title,
                                it.overview,
                                it.popularity,
                                it.firstAirDate,
                                it.numberOfEpisodes,
                                it.numberOfSeasons,
                                it.originalLanguage,
                                it.isFavorite
                            ))
                        }
                    }
                }
            }
        }
    }

    private fun setMovieDataDetail(movieState: ApiResponse<Movie>){
        when(movieState){
            is SuccessResponse -> {
                val data = movieState.body!!
                Log.d(TAG, "populateMovies: isi movie list $data")

                binding.containerContent.visible()
                binding.tvNoDataDetail.gone()
                binding.imgNoDataDetail.gone()
                movieData = data
                populateMovieDataContentDetail(data)

                binding.progressBar.gone()
            }
            is EmptyResponse -> {
                Toast.makeText(this, "No Data Collected", Toast.LENGTH_SHORT).show()
                binding.tvNoDataDetail.text = movieState.message
                binding.containerContent.gone()
                binding.imgNoDataDetail.visible()
                binding.tvNoDataDetail.visible()
            }
            is LoadingResponse ->{
                binding.progressBar.visible()
            }
            is ErrorResponse -> {
                Toast.makeText(this, "Error collecting data", Toast.LENGTH_SHORT).show()
                binding.tvNoDataDetail.text = movieState.message
                binding.containerContent.gone()
                binding.imgNoDataDetail.visible()
                binding.tvNoDataDetail.visible()
                binding.progressBar.gone()
            }
        }
    }

    private fun setTvDataDetail(tvState: ApiResponse<TvShow>){
        when(tvState){
            is SuccessResponse -> {
                if(tvState.body != null){
                    val data = tvState.body
                    Log.d(TAG, "populateMovies: isi movie list $data")

                    populateTvShowDataContentDetail(data)
                    tvData = data
                    binding.containerContent.visible()
                    binding.tvNoDataDetail.gone()
                    binding.imgNoDataDetail.gone()
                    binding.progressBar.gone()
                }
            }
            is EmptyResponse -> {
                Toast.makeText(this, "No Data Collected", Toast.LENGTH_SHORT).show()
                binding.tvNoDataDetail.text = tvState.message
                binding.imgNoDataDetail.visible()
                binding.tvNoDataDetail.visible()
                binding.containerContent.gone()
                binding.progressBar.gone()
            }
            is LoadingResponse ->{
                binding.progressBar.visible()
            }
            is ErrorResponse -> {
                Toast.makeText(this, "Error collecting data", Toast.LENGTH_SHORT).show()
                binding.tvNoDataDetail.text = tvState.message
                binding.imgNoDataDetail.visible()
                binding.tvNoDataDetail.visible()
                binding.containerContent.gone()
                binding.progressBar.gone()
            }
        }

    }

    private fun populateMovieDataContentDetail(movie: Movie?){
        with(bindingContentDetail){
            imageView.load(BASE_URL_MOVIEDB_IMAGE + movie?.posterPath){
                placeholder(R.drawable.account_box)
                error(R.drawable.error)
                crossfade(true)
                crossfade(350)
                transformations(RoundedCornersTransformation(8f))
            }
            tvIdDetail.text = movie?.movieId
            tvTitleDetail.text = (movie?.title ?: R.string.no_data).toString()
            tvReleaseDetail.text = (movie?.releaseDate ?: R.string.no_data).toString()
            tvPopularityDetail.text = (movie?.popularity ?: R.string.no_data).toString()
            tvOverviewDetail.text = (movie?.overview ?: R.string.no_data).toString()
            tvLanguageDetail.text = (movie?.originalLanguage?.toEngLang() ?: R.string.no_data).toString()

        }
    }

    private fun populateTvShowDataContentDetail(tvshow: TvShow?){
        with(bindingContentDetail){
            nmbOfEps.visibility = View.VISIBLE
            nmbOfSeas.visibility = View.VISIBLE
            imageView.load(BASE_URL_MOVIEDB_IMAGE + tvshow?.posterPath){
                placeholder(R.drawable.account_box)
                error(R.drawable.error)
                crossfade(true)
                crossfade(350)
                transformations(RoundedCornersTransformation(8f))
            }
            tvIdDetail.text = tvshow?.tvId
            tvTitleDetail.text = tvshow?.title ?: R.string.no_data.toString()
            tvReleaseDetail.text = tvshow?.firstAirDate ?: R.string.no_data.toString()
            tvPopularityDetail.text = tvshow?.popularity ?: R.string.no_data.toString()
            tvOverviewDetail.text = tvshow?.overview ?: R.string.no_data.toString()
            tvNumberOfEps.text = (tvshow?.numberOfEpisodes ?: R.string.no_data.toString()).toString()
            tvNumberOfSeasons.text = (tvshow?.numberOfSeasons ?: R.string.no_data.toString()).toString()
            tvLanguageDetail.text = (tvshow?.originalLanguage?.toEngLang() ?: R.string.no_data.toString()).toString()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        this.menu = menu

        isFavoriteActive(isFav)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_action -> {
                if(type == MOVIE_TYPE) {
                    if(isFav) {
                        viewModel.deleteMovieFav(movieData)
                        Toast.makeText(this, "Deleted from favorite", Toast.LENGTH_SHORT).show()
                    }

                    else {
                        viewModel.setMovieFavorite(movieData)
                        Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
                    }
                } else if(type == TV_TYPE){
                    if(isFav) {
                        viewModel.deleteTvFav(tvData)
                        Toast.makeText(this, "Deleted from favorite", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        viewModel.setTvFavorite(tvData)
                        Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
                    }
                }
                isFav = !isFav
                isFavoriteActive(isFav)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun isFavoriteActive(state: Boolean){
        if(this.menu != null){
            Log.d(TAG, "isFavoriteActive: alhamdulillaah keubah $menu")
            val favoriteButton = this.menu?.findItem(R.id.favorite_action)
            if(state) favoriteButton?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
            else favoriteButton?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border)
        }
    }
}