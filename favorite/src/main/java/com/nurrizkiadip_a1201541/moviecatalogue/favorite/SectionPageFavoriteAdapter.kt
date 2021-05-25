package com.nurrizkiadip_a1201541.moviecatalogue.favorite

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nurrizkiadip_a1201541.moviecatalogue.favorite.moviefavorite.MovieFavoriteFragment
import com.nurrizkiadip_a1201541.moviecatalogue.favorite.tvshowfavorite.TvShowFavoriteFragment

class SectionPageFavoriteAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                MovieFavoriteFragment()
            }
            1 -> {
                TvShowFavoriteFragment()
            }
            else -> Fragment()
        }
    }

}
