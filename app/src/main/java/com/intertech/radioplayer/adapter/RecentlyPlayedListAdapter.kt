package com.intertech.radioplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intertech.radioplayer.R
import com.intertech.radioplayer.data.model.RadioPlayList
import com.intertech.radioplayer.databinding.AdapterRecentlyPlayedListBinding

class RecentlyPlayedListAdapter(
    private val context: Context,
    private val playedList: List<RadioPlayList.RadioPlayListItem>
) : RecyclerView.Adapter<RecentlyPlayedListAdapter.PlayListViewHolder>() {


    //the class is holding the list view
    class PlayListViewHolder(
        val recentlyPlayedListBinding: AdapterRecentlyPlayedListBinding
    ) : RecyclerView.ViewHolder(recentlyPlayedListBinding.root) {

    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlayListViewHolder(
            AdapterRecentlyPlayedListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        // Display list item
        val playedListData = playedList[position]
        holder.recentlyPlayedListBinding.tvSongTitle.text = playedListData.name
        holder.recentlyPlayedListBinding.tvArtistName.text = playedListData.artist
        Glide
            .with(context)
            .load(playedListData.image_url)
            .centerCrop()
            .placeholder(R.drawable.music_placeholder)
            .into(holder.recentlyPlayedListBinding.ivAlbumArt)
    }

    //this method is giving the size of the list
    override fun getItemCount() = playedList.size
}