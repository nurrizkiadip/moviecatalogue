package com.nurrizkiadip_a1201541.moviecatalogue.ui.tvshows

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
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.FragmentTvShowsCatalogueBinding
import com.nurrizkiadip_a1201541.moviecatalogue.viewmodel.ViewModelFactory
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible
import com.nurrizkiadip_a1201541.moviecatalogue.vo.*

class TvShowsCatalogueFragment : Fragment(){

    private var _binding: FragmentTvShowsCatalogueBinding? = null
    private val binding get() = _binding as FragmentTvShowsCatalogueBinding

    private lateinit var adapter: TvShowsAdapter
    private lateinit var viewModel: TvShowsViewModel

    companion object{
        private val TAG: String = TvShowsCatalogueFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTvShowsCatalogueBinding.inflate(inflater, container, false)
        adapter = TvShowsAdapter()
        val factory = ViewModelFactory.getInstance(requireActivity(), requireActivity().application, lifecycleScope)
        viewModel = ViewModelProvider(requireActivity(), factory)[TvShowsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visible()
        if(activity != null){
            populateTvs()

            binding.rvTvshow.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@TvShowsCatalogueFragment.adapter
            }
        }
    }

    private fun populateTvs() {
        viewModel.getAllTvShows().observe(viewLifecycleOwner){
            if (it != null) when(it){
                is SuccessResponse -> {
                    if(it.body != null){
                        Log.d(TAG, "populateTvs: list tv: \n${it.body[0]}")
                        Toast.makeText(requireActivity(), "Success Collecting Data", Toast.LENGTH_SHORT).show()
                        binding.tvNoData.gone()
                        binding.imgNoData.gone()
                        binding.progressBar.gone()
                        adapter.setTvShows(it.body)
                    }
                }
                is EmptyResponse -> {
                    Log.d(TAG, "populateTvs: list tv: \n${it.message}")
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
                    Log.d(TAG, "populateTvs: list tv: \n${it.message}")
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