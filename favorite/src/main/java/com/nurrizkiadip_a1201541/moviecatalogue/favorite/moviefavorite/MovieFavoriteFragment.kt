package com.nurrizkiadip_a1201541.moviecatalogue.favorite.moviefavorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurrizkiadip_a1201541.moviecatalogue.R
import com.nurrizkiadip_a1201541.moviecatalogue.core.ui.MoviesAdapter
import com.nurrizkiadip_a1201541.moviecatalogue.detail.DetailActivity
import com.nurrizkiadip_a1201541.moviecatalogue.detail.DetailActivity.Companion.MOVIE_TYPE
import com.nurrizkiadip_a1201541.moviecatalogue.favorite.FavoriteViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.favorite.databinding.FragmentMovieFavoriteBinding
import com.nurrizkiadip_a1201541.moviecatalogue.nav_ui.movies.MoviesCatalogueFragment
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible

class MovieFavoriteFragment : Fragment() {

    private var _binding: FragmentMovieFavoriteBinding? = null
    private val binding get() = _binding as FragmentMovieFavoriteBinding

    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var adapter: MoviesAdapter

    companion object{
        private val TAG: String = MovieFavoriteFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieFavoriteBinding.inflate(
            inflater, container, false
        )
        adapter = MoviesAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            adapter.onItemClick = {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DETAIL_TYPE, MOVIE_TYPE)
                intent.putExtra(DetailActivity.EXTRA_DATA, it)
                startActivity(intent)
            }

            populateMoviesFav()

            binding.rvMovies.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@MovieFavoriteFragment.adapter
            }

            //Search Movie
            binding.btnSearch.setOnClickListener {
                binding.rvMovies.visible()
                binding.progressBar.visible()
                val searchString = binding.edtSearch.text.toString()

                if (searchString.isNotEmpty())
                    viewModel.getMoviesBySearch("%$searchString%").observe(viewLifecycleOwner){
                        if(it != null){
                            Log.d(TAG, "onViewCreated: onclick search isi list fav ${it.toList()}")
                            if(it.isNotEmpty()){
                                binding.imgNoData.gone()
                                binding.tvNoData.gone()
                            } else {
                                binding.tvNoData.text = getString(R.string.no_data)
                                binding.rvMovies.gone()
                                binding.imgNoData.visible()
                                binding.tvNoData.visible()
                            }
                            adapter.setMovies(it)
                        }
                    }
                else populateMoviesFav()

                adapter.notifyDataSetChanged()
                binding.progressBar.gone()
            }
        }
    }

    private fun populateMoviesFav() {
        viewModel.allMovieFavorite.observe(viewLifecycleOwner){
            if(it != null){
                if(it.isEmpty()){
                    binding.tvNoData.visible()
                    binding.imgNoData.visible()
                } else {
                    binding.tvNoData.gone()
                    binding.imgNoData.gone()
                }
                adapter.setMovies(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}