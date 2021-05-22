package com.nurrizkiadip_a1201541.moviecatalogue.ui.favorite.moviefavorite

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.FragmentMovieFavoriteBinding
import com.nurrizkiadip_a1201541.moviecatalogue.ui.favorite.FavoriteViewModel
import com.nurrizkiadip_a1201541.moviecatalogue.utils.gone
import com.nurrizkiadip_a1201541.moviecatalogue.utils.visible
import com.nurrizkiadip_a1201541.moviecatalogue.viewmodel.ViewModelFactory

class MovieFavoriteFragment : Fragment() {

    private var _binding: FragmentMovieFavoriteBinding? = null
    private val binding get() = _binding as FragmentMovieFavoriteBinding

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: MovieFavoriteAdapter

    companion object{
        private val TAG: String = MovieFavoriteFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieFavoriteBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity(), requireActivity().application, lifecycleScope)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
        adapter = MovieFavoriteAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTouchHelper.attachToRecyclerView(binding.rvMovies)

        if(activity != null){
            binding.progressBar.visible()
            viewModel.getAllMovieFavorite().observe(viewLifecycleOwner){
                if(it != null){
                    binding.progressBar.gone()
                    Log.d(TAG, "onViewCreated: isi list fav ${it.toList()}")
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

            binding.rvMovies.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = this@MovieFavoriteFragment.adapter
            }

            binding.btnSearch.setOnClickListener {
                binding.progressBar.visible()
                val searchString = binding.edtSearch.text.toString()
                viewModel.getMoviesBySearch("%$searchString%").observe(viewLifecycleOwner){
                    if(it != null){
                        Log.d(TAG, "onViewCreated: onclick search isi list fav ${it.toList()}")
                        if(it.size > 0){
                            binding.imgNoData.gone()
                            binding.tvNoData.gone()
                            adapter.submitList(it)
                        } else {
                            adapter.submitList(it)
                            binding.imgNoData.visible()
                            binding.tvNoData.visible()
                        }
                        binding.progressBar.gone()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }



    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val courseEntity = adapter.getSwipedData(swipedPosition)
                courseEntity?.let { viewModel.deleteMovieFavorite(it) }
                Toast.makeText(requireActivity(), "Movie Favorite Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    })


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}