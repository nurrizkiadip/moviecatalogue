package com.nurrizkiadip_a1201541.moviecatalogue.nav_ui.tvshows

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
import com.nurrizkiadip_a1201541.moviecatalogue.core.ui.TvShowsAdapter
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.FragmentTvShowsCatalogueBinding
import com.nurrizkiadip_a1201541.moviecatalogue.detail.DetailActivity
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible

class TvShowsCatalogueFragment : Fragment(){

    private var _binding: FragmentTvShowsCatalogueBinding? = null
    private val binding get() = _binding as FragmentTvShowsCatalogueBinding

    private lateinit var adapter: TvShowsAdapter
    private val viewModel: TvShowsViewModel by viewModel()

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
//        val factory = ViewModelFactory.getInstance(requireActivity(), requireActivity().application, lifecycleScope)
//        viewModel = ViewModelProvider(requireActivity(), factory)[TvShowsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visible()
        if(activity != null){
            adapter.onItemClick = {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DETAIL_TYPE, DetailActivity.TV_TYPE)
                intent.putExtra(DetailActivity.EXTRA_DATA, it)
                startActivity(intent)
            }
            populateTvs()

            binding.rvTvshow.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@TvShowsCatalogueFragment.adapter
            }

            //Tv Search
            binding.btnSearch.setOnClickListener {
                binding.rvTvshow.visible()
                binding.progressBar.visible()
                val searchString = binding.edtSearch.text.toString()

                if (searchString.isNotEmpty())
                    viewModel.getListTvsSearch("%$searchString%").observe(viewLifecycleOwner){
                    if(it != null){
                        Log.d(TAG, "onViewCreated: onclick search isi list fav ${it.toList()}")
                        if(it.isNotEmpty()){
                            binding.imgNoData.gone()
                            binding.tvNoData.gone()
                        } else {
                            binding.tvNoData.text = getString(R.string.no_data)
                            binding.rvTvshow.gone()
                            binding.imgNoData.visible()
                            binding.tvNoData.visible()
                        }
                        adapter.setTvShows(it)
                    }
                }
                else populateTvs()

                adapter.notifyDataSetChanged()
                binding.progressBar.gone()
            }
        }
    }

    private fun populateTvs() {
        viewModel.allTvShows.observe(viewLifecycleOwner){
            if (it != null) when(it){
                is Resource.Success -> {
                    if(it.data != null){
                        Log.d(TAG, "populateTvs: list tv: \n${it.data!![0]}")
                        Toast.makeText(requireActivity(), "Success Collecting Data", Toast.LENGTH_SHORT).show()
                        adapter.setTvShows(it.data!!)
                        binding.tvNoData.gone()
                        binding.imgNoData.gone()
                        binding.progressBar.gone()
                    }
                }
                is Resource.Empty -> {
                    Log.d(TAG, "populateTvs: list tv: \n${it.message}")
                    Toast.makeText(requireActivity(), "No Data Collected", Toast.LENGTH_SHORT).show()
                    binding.tvNoData.text = it.message
                    binding.imgNoData.visible()
                    binding.tvNoData.visible()
                    binding.progressBar.gone()
                }
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.tvNoData.gone()
                    binding.imgNoData.gone()
                }
                is Resource.Error -> {
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