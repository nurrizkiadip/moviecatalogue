package com.nurrizkiadip_a1201541.moviecatalogue.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.nurrizkiadip_a1201541.moviecatalogue.core.BuildConfig.BASE_URL_MOVIEDB_IMAGE
import com.nurrizkiadip_a1201541.moviecatalogue.core.R
import com.nurrizkiadip_a1201541.moviecatalogue.core.databinding.ItemMoviesCatalogueBinding
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.Movie

class MoviesAdapter: RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    val listMovies = ArrayList<Movie>()
    var onItemClick: ((Movie) -> Unit)? = null

    fun setMovies(list: List<Movie>) {
        this.listMovies.clear()
        this.listMovies.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMoviesCatalogueBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = listMovies[position]
        holder.bind(movie)

    }

    override fun getItemCount(): Int = listMovies.size

    inner class ViewHolder(
        private val binding: ItemMoviesCatalogueBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                tvTitleMovies.text = (movie.title ?: R.string.no_data).toString()
                tvReleasedateMovies.text = (movie.releaseDate ?: R.string.no_data).toString()
                imgMovies.load(BASE_URL_MOVIEDB_IMAGE + movie.posterPath) {
                    placeholder(R.drawable.account_box)
                    error(R.drawable.error)
                    crossfade(true)
                    crossfade(250)
                    transformations(RoundedCornersTransformation(8f, 0f, 8f, 0f))
                }

            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listMovies[adapterPosition])
            }
        }
    }
}