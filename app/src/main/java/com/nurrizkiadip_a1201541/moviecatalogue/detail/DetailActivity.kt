package com.nurrizkiadip_a1201541.moviecatalogue.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.RoundedCornersTransformation
import com.nurrizkiadip_a1201541.moviecatalogue.R
import com.nurrizkiadip_a1201541.moviecatalogue.core.BuildConfig.BASE_URL_MOVIEDB_IMAGE
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.ActivityDetailBinding
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.ContentDetailBinding
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.toEngLang
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var bindingContentDetail: ContentDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    private lateinit var type: String
    private var isFav: Boolean = false
    private var menu: Menu? = null
    private var movieData: Movie? = null
    private var tvData: TvShow? = null

    companion object {
        private val TAG: String = DetailActivity::class.java.simpleName
        const val MOVIE_TYPE = "movie_type"
        const val TV_TYPE = "tv_type"
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DETAIL_TYPE = "extra_detail_type"
//        const val EXTRA_ID = "extra_id"
//        const val EXTRA_DETAIL_IS_FAV = "extra_detail_is_fav"
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

        type = intent.getStringExtra(EXTRA_DETAIL_TYPE) ?: ""
        Log.d(TAG, "onCreate: type: $type")

        binding.progressBar.visible()
        if(type == MOVIE_TYPE){
            movieData = intent.getParcelableExtra(EXTRA_DATA)
            if(movieData != null){
                Log.d(TAG, "onCreate: tipe movie sudah sampai sini")
                isFav = movieData!!.isFavorite
                populateMovieDataContentDetail(movieData)
                binding.progressBar.gone()
            }
        }
        else if(type == TV_TYPE){
            tvData = intent.getParcelableExtra(EXTRA_DATA)
            if(tvData != null){
                Log.d(TAG, "onCreate: tipe tv sudah sampai sini")
                isFav = tvData!!.isFavorite
                populateTvShowDataContentDetail(tvData)
                binding.progressBar.gone()

            }
        }
    }

    private fun populateMovieDataContentDetail(movie: Movie?){
        with(bindingContentDetail){
            movie?.let {
                imageView.load(BASE_URL_MOVIEDB_IMAGE + movie.posterPath){
                    placeholder(R.drawable.account_box)
                    error(R.drawable.error)
                    crossfade(true)
                    crossfade(350)
                    transformations(RoundedCornersTransformation(8f))
                }
                tvIdDetail.text = movie.movieId
                tvTitleDetail.text = (movie.title ?: R.string.no_data).toString()
                tvReleaseDetail.text = (movie.releaseDate ?: R.string.no_data).toString()
                tvPopularityDetail.text = (movie.popularity ?: R.string.no_data).toString()
                tvOverviewDetail.text = (movie.overview ?: R.string.no_data).toString()
                tvLanguageDetail.text = (movie.originalLanguage?.toEngLang() ?: R.string.no_data).toString()

            }
        }
    }

    private fun populateTvShowDataContentDetail(tvshow: TvShow?){
        with(bindingContentDetail){
            tvshow?.let {
                imageView.load(BASE_URL_MOVIEDB_IMAGE + tvshow.posterPath){
                    placeholder(R.drawable.account_box)
                    error(R.drawable.error)
                    crossfade(true)
                    crossfade(350)
                    transformations(RoundedCornersTransformation(8f))
                }
                tvIdDetail.text = tvshow.tvId
                tvTitleDetail.text = tvshow.title ?: R.string.no_data.toString()
                tvReleaseDetail.text = tvshow.firstAirDate ?: R.string.no_data.toString()
                tvPopularityDetail.text = tvshow.popularity ?: R.string.no_data.toString()
                tvOverviewDetail.text = tvshow.overview ?: R.string.no_data.toString()
                tvLanguageDetail.text = (tvshow.originalLanguage?.toEngLang() ?: R.string.no_data.toString()).toString()

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        this.menu = menu

        if(type == MOVIE_TYPE)
            isFavoriteActive(isFav)
        else
            isFavoriteActive(isFav)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_action -> {
                isFav = !isFav
                if(type == MOVIE_TYPE) {
                    viewModel.setMovieFavorite(movieData!!, isFav)
                } else if(type == TV_TYPE){
                    viewModel.setTvFavorite(tvData!!, isFav)
                }
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
//    private fun setMovieDataDetail(movieState: Movie){
//        when(movieState){
//            is Resource.Success -> {
//                val data = movieState.data
//                Log.d(TAG, "populateMovies: isi movie list $data")
//
//                movieData = data!!
//                populateMovieDataContentDetail(data)
//
//                binding.containerContent.visible()
//                binding.tvNoDataDetail.gone()
//                binding.imgNoDataDetail.gone()
//                binding.progressBar.gone()
//            }
//            is Resource.Empty -> {
//                Toast.makeText(this, "No Data Collected", Toast.LENGTH_SHORT).show()
//                binding.tvNoDataDetail.text = movieState.message
//                binding.imgNoDataDetail.visible()
//                binding.tvNoDataDetail.visible()
//                binding.containerContent.gone()
//                binding.progressBar.gone()
//            }
//            is Resource.Loading ->{
//                binding.progressBar.visible()
//                binding.imgNoDataDetail.gone()
//                binding.tvNoDataDetail.gone()
//                binding.progressBar.gone()
//            }
//            is Resource.Error -> {
//                Toast.makeText(this, "Error collecting data", Toast.LENGTH_SHORT).show()
//                binding.tvNoDataDetail.text = movieState.message
//                binding.containerContent.gone()
//                binding.imgNoDataDetail.visible()
//                binding.tvNoDataDetail.visible()
//                binding.progressBar.gone()
//            }
//        }
//    }
//
//    private fun setTvDataDetail(tvState: Resource<TvShow>){
//        when(tvState){
//            is Resource.Success -> {
//                val data = tvState.data
//                Log.d(TAG, "populateMovies: isi movie list $data")
//
//                populateTvShowDataContentDetail(data)
//                tvData = data!!
//                binding.containerContent.visible()
//                binding.tvNoDataDetail.gone()
//                binding.imgNoDataDetail.gone()
//                binding.progressBar.gone()
//
//            }
//            is Resource.Empty -> {
//                Toast.makeText(this, "No Data Collected", Toast.LENGTH_SHORT).show()
//                binding.tvNoDataDetail.text = tvState.message
//                binding.imgNoDataDetail.visible()
//                binding.tvNoDataDetail.visible()
//                binding.containerContent.gone()
//                binding.progressBar.gone()
//            }
//            is Resource.Loading ->{
//                binding.progressBar.visible()
//                binding.containerContent.visible()
//                binding.tvNoDataDetail.gone()
//                binding.imgNoDataDetail.gone()
//            }
//            is Resource.Error -> {
//                Toast.makeText(this, "Error collecting data", Toast.LENGTH_SHORT).show()
//                binding.tvNoDataDetail.text = tvState.message
//                binding.imgNoDataDetail.visible()
//                binding.tvNoDataDetail.visible()
//                binding.containerContent.gone()
//                binding.progressBar.gone()
//            }
//        }
//    }