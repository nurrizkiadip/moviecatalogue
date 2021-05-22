package com.nurrizkiadip_a1201541.moviecatalogue.ui.favorite.tvshowfavorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.FragmentTvShowFavoriteBinding
import com.nurrizkiadip_a1201541.moviecatalogue.ui.favorite.FavoriteViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible
import com.nurrizkiadip_a1201541.moviecatalogue.viewmodel.ViewModelFactory

class TvShowFavoriteFragment : Fragment() {

    private var _binding: FragmentTvShowFavoriteBinding? = null
    private val binding get() = _binding as FragmentTvShowFavoriteBinding

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: TvShowFavoriteAdapter

    companion object{
        private val TAG: String = TvShowFavoriteFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTvShowFavoriteBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity(), requireActivity().application, lifecycleScope)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
        adapter = TvShowFavoriteAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTouchHelper.attachToRecyclerView(binding.rvTvshow)

        if(activity != null){
            binding.progressBar.visible()
            viewModel.getAllTvFavorite().observe(viewLifecycleOwner){
                if(it != null){
                    binding.progressBar.gone()
                    if(it.size > 0){
                        binding.imgNoData.gone()
                        binding.tvNoData.gone()
                        adapter.submitList(it)

                    } else {
                        binding.imgNoData.visible()
                        binding.tvNoData.visible()
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            binding.rvTvshow.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@TvShowFavoriteFragment.adapter
            }

            binding.btnSearch.setOnClickListener {
                binding.progressBar.visible()
                val searchString = binding.edtSearch.text.toString()
                viewModel.getTvsBySearch("%$searchString%").observe(viewLifecycleOwner){
                    if(it != null){
                        Log.d(TAG, "onViewCreated: onclick search isi list fav ${it.toList()}")
                        if(it.size > 0){
                            binding.imgNoData.gone()
                            binding.tvNoData.gone()
                        } else {
                            binding.imgNoData.visible()
                            binding.tvNoData.visible()
                        }
                        adapter.submitList(it)
                        binding.progressBar.gone()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val courseEntity = adapter.getSwipedData(swipedPosition)
                courseEntity?.let { viewModel.deleteTvFavorite(it) }
                Toast.makeText(requireActivity(), "Tv Favorite Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    })

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}