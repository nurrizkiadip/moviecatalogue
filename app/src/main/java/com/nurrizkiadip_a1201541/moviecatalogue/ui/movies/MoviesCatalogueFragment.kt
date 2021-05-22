package com.nurrizkiadip_a1201541.moviecatalogue.ui.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.EmptyResponse
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.ErrorResponse
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.LoadingResponse
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.remote.SuccessResponse
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.FragmentMoviesCatalogueBinding
import com.nurrizkiadip_a1201541.moviecatalogue.viewmodel.ViewModelFactory
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible
import com.nurrizkiadip_a1201541.moviecatalogue.vo.*

class MoviesCatalogueFragment : Fragment(){

    private var _binding: FragmentMoviesCatalogueBinding? = null
    private val binding get() = _binding as FragmentMoviesCatalogueBinding
    private lateinit var adapter: MoviesCatalogueAdapter
    private lateinit var viewModel: MoviesCatalogueViewModel
    
    companion object{
        val TAG: String = MoviesCatalogueFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesCatalogueBinding.inflate(inflater, container, false)

        adapter = MoviesCatalogueAdapter()

        val factory = ViewModelFactory.getInstance(requireActivity(), requireActivity().application, lifecycleScope)
        viewModel = ViewModelProvider(requireActivity(), factory)[MoviesCatalogueViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visible()
        if(activity != null){
            populateMovies()

            binding.rvMovies.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@MoviesCatalogueFragment.adapter
            }
        }
    }

    private fun populateMovies() {
        viewModel.getMoviesData().observe(viewLifecycleOwner){
            if(it != null) when(it){
                is SuccessResponse -> {
                    if(it.body != null){
                        Toast.makeText(requireActivity(), "Success Collecting Data", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "populateMovies: isi movie list ${it.body[0]}")
                        binding.tvNoData.gone()
                        binding.imgNoData.gone()
                        adapter.setMovies(it.body)

                        binding.progressBar.gone()
                    }

                }
                is EmptyResponse -> {
                    Toast.makeText(requireActivity(), "No Data Collected", Toast.LENGTH_SHORT).show()
                    binding.tvNoData.text = it.message
                    binding.imgNoData.visible()
                    binding.tvNoData.visible()
                    binding.progressBar.gone()
                }
                is LoadingResponse -> {
                    binding.progressBar.visible()
                }
                is ErrorResponse -> {
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