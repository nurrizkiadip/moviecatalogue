package com.nurrizkiadip_a1201541.moviecatalogue.favorite.tvshowfavorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurrizkiadip_a1201541.moviecatalogue.R
import com.nurrizkiadip_a1201541.moviecatalogue.core.ui.TvShowsAdapter
import com.nurrizkiadip_a1201541.moviecatalogue.detail.DetailActivity
import com.nurrizkiadip_a1201541.moviecatalogue.detail.DetailActivity.Companion.TV_TYPE
import com.nurrizkiadip_a1201541.moviecatalogue.favorite.FavoriteViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.favorite.databinding.FragmentTvShowFavoriteBinding
import com.nurrizkiadip_a1201541.moviecatalogue.nav_ui.movies.MoviesCatalogueFragment
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible

class TvShowFavoriteFragment : Fragment() {

    private var _binding: FragmentTvShowFavoriteBinding? = null
    private val binding get() = _binding as FragmentTvShowFavoriteBinding

    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var adapter: TvShowsAdapter

    companion object{
        private val TAG: String = TvShowFavoriteFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTvShowFavoriteBinding.inflate(inflater, container, false)
        adapter = TvShowsAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            adapter.onItemClick = {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DETAIL_TYPE, TV_TYPE)
                intent.putExtra(DetailActivity.EXTRA_DATA, it)
                startActivity(intent)
            }

            populateTvFav()

            binding.rvTvshow.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@TvShowFavoriteFragment.adapter
            }

            //Tv Search
            binding.btnSearch.setOnClickListener {
                binding.rvTvshow.visible()
                binding.progressBar.visible()
                val searchString = binding.edtSearch.text.toString()

                if (searchString.isNotEmpty())
                    viewModel.getTvsBySearch("%$searchString%").observe(viewLifecycleOwner){
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
                else populateTvFav()

                adapter.notifyDataSetChanged()
                binding.progressBar.gone()
            }
        }
    }

    private fun populateTvFav() {
        viewModel.allTvFavorite.observe(viewLifecycleOwner){
            if(it != null){
                if(it.isEmpty()){
                    binding.tvNoData.visible()
                    binding.imgNoData.visible()
                } else {
                    binding.tvNoData.gone()
                    binding.imgNoData.gone()
                }
                adapter.setTvShows(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}