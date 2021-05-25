package com.nurrizkiadip_a1201541.moviecatalogue.nav_ui.movies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurrizkiadip_a1201541.moviecatalogue.R
import com.nurrizkiadip_a1201541.moviecatalogue.core.data.Resource
import com.nurrizkiadip_a1201541.moviecatalogue.core.ui.MoviesAdapter
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.FragmentMoviesCatalogueBinding
import com.nurrizkiadip_a1201541.moviecatalogue.detail.DetailActivity
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible

class MoviesCatalogueFragment : Fragment(){

    private var _binding: FragmentMoviesCatalogueBinding? = null
    private val binding get() = _binding as FragmentMoviesCatalogueBinding
    private lateinit var adapter: MoviesAdapter
    private val viewModel: MovieViewModel by viewModel()
    
    companion object{
        val TAG: String = MoviesCatalogueFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesCatalogueBinding.inflate(inflater, container, false)

        adapter = MoviesAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visible()
        if(activity != null){
            adapter.onItemClick = {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DETAIL_TYPE, DetailActivity.MOVIE_TYPE)
                intent.putExtra(DetailActivity.EXTRA_DATA, it)
                startActivity(intent)
            }

            populateMovies()

            binding.rvMovies.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@MoviesCatalogueFragment.adapter
            }

            //Movie Search
            binding.btnSearch.setOnClickListener {
                binding.rvMovies.visible()
                binding.progressBar.visible()
                val searchString = binding.edtSearch.text.toString()

                if (searchString.isNotEmpty())
                    viewModel.getListMoviesSearch("%$searchString%").observe(viewLifecycleOwner){
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
                else populateMovies()

                adapter.notifyDataSetChanged()
                binding.progressBar.gone()
            }
        }
    }

    private fun populateMovies() {
        viewModel.listMovies.observe(viewLifecycleOwner){
            if(it != null) when(it){
                is Resource.Success -> {
                    if(it.data != null){
                        Toast.makeText(requireActivity(), "Success Collecting Data", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "populateMovies: isi movie list ${it.data!![0]}")
                        adapter.setMovies(it.data!!)
                        binding.tvNoData.gone()
                        binding.imgNoData.gone()
                        binding.progressBar.gone()
                    }
                }
                is Resource.Empty -> {
                    Toast.makeText(requireActivity(), "No Data Collected", Toast.LENGTH_SHORT).show()
                    binding.tvNoData.text = it.message
                    binding.imgNoData.visible()
                    binding.tvNoData.visible()
                    binding.progressBar.gone()
                }
                is Resource.Loading -> {
                    binding.tvNoData.gone()
                    binding.imgNoData.gone()
                    binding.progressBar.visible()
                }
                is Resource.Error -> {
                    Toast.makeText(requireActivity(), "Error collecting data", Toast.LENGTH_SHORT).show()
                    binding.tvNoData.text = it.message
                    binding.imgNoData.visible()
                    binding.tvNoData.visible()
                    binding.progressBar.gone()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}