package com.nurrizkiadip_a1201541.moviecatalogue.ui.favorite.tvshowfavorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.nurrizkiadip_a1201541.moviecatalogue.BuildConfig.BASE_URL_MOVIEDB_IMAGE
import com.nurrizkiadip_a1201541.moviecatalogue.R
import com.nurrizkiadip_a1201541.moviecatalogue.data.source.local.entity.TvShowEntity
import com.nurrizkiadip_a1201541.moviecatalogue.databinding.ItemTvShowsBinding
import com.nurrizkiadip_a1201541.moviecatalogue.ui.detail.DetailActivity
import com.nurrizkiadip_a1201541.moviecatalogue.ui.detail.DetailActivity.Companion.TV_TYPE

class TvShowFavoriteAdapter : PagedListAdapter<TvShowEntity, TvShowFavoriteAdapter.TvShowViewHolder>(DIFF_CALLBACK){

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShowEntity>() {
            override fun areItemsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem.tvId == newItem.tvId
            }

            override fun areContentsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        return TvShowViewHolder(ItemTvShowsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        val tvShow = getItem(position)
        if(tvShow != null) holder.bind(tvShow)
    }

    fun getSwipedData(swipedPosition: Int): TvShowEntity? = getItem(swipedPosition)

    inner class TvShowViewHolder(private val binding: ItemTvShowsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShowEntity) {
            with(binding){
                tvTitleTvshows.text = (tvShow.title ?: R.string.no_data).toString()
                tvReleasedateTvshows.text = (tvShow.firstAirDate ?: R.string.no_data).toString()
                imgTvshows.load(BASE_URL_MOVIEDB_IMAGE + tvShow.posterPath){
                    placeholder(R.drawable.account_box)
                    error(R.drawable.error)
                    crossfade(true)
                    crossfade(350)
                    transformations(RoundedCornersTransformation(8f,0f,8f,0f))
                }

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ID, tvShow.tvId)
                    intent.putExtra(DetailActivity.EXTRA_DETAIL_TYPE, TV_TYPE)
                    intent.putExtra(DetailActivity.EXTRA_DETAIL_IS_FAV, true)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

}
