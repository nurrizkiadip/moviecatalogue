package com.nurrizkiadip_a1201541.moviecatalogue.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.nurrizkiadip_a1201541.moviecatalogue.core.BuildConfig.BASE_URL_MOVIEDB_IMAGE
import com.nurrizkiadip_a1201541.moviecatalogue.core.R
import com.nurrizkiadip_a1201541.moviecatalogue.core.databinding.ItemTvShowsBinding
import com.nurrizkiadip_a1201541.moviecatalogue.core.domain.model.TvShow

class TvShowsAdapter : RecyclerView.Adapter<TvShowsAdapter.TvShowViewHolder>(){

    private val listTvShows = ArrayList<TvShow>()
    var onItemClick: ((TvShow) -> Unit)? = null

    fun setTvShows(list: List<TvShow>){
        this.listTvShows.clear()
        this.listTvShows.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        return TvShowViewHolder(ItemTvShowsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        val tvShow = listTvShows[position]
        holder.bind(tvShow)

    }

    override fun getItemCount(): Int = listTvShows.size

    inner class TvShowViewHolder(
        private val binding: ItemTvShowsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShow) {
            with(binding){
                tvTitleTvshows.text = (tvShow.title ?: R.string.no_data).toString()
                tvReleasedateTvshows.text = (tvShow.firstAirDate ?: R.string.no_data).toString()
                imgTvshows.load(BASE_URL_MOVIEDB_IMAGE + tvShow.posterPath){
                    placeholder(R.drawable.account_box)
                    error(R.drawable.error)
                    crossfade(true)
                    crossfade(250)
                    transformations(RoundedCornersTransformation(8f,0f,8f,0f))
                }
            }

        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listTvShows[adapterPosition])
            }
        }
    }

}
