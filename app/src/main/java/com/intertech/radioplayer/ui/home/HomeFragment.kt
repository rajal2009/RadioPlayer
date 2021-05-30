package com.intertech.radioplayer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.intertech.radioplayer.R
import com.intertech.radioplayer.data.model.RadioPlayList
import com.intertech.radioplayer.data.network.Resource
import com.intertech.radioplayer.data.network.handleApiError
import com.intertech.radioplayer.databinding.FragmentHomeBinding
import com.intertech.radioplayer.ui.base.BaseFragment
import com.intertech.radioplayer.ui.main.PlayListViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>()  {

    /**
     * Get activity view model
     */
    private val fragmentViewModel: PlayListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //2. The “Home” View will display the song title, artist name, and album art of the song currently playing on the radio stream. 
        /**
         * Observe api data response
         */
        fragmentViewModel.playListResponse.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Resource.Success -> {
                    /**
                     * Hide progressbar dialog
                     */
                    dismissProgressDialog()

                    /**
                     * Received data from server
                     */
                    val playListData = Gson().toJson(it.value)

                    /**
                     * Convert received data string into array list
                     */
                    val playedListArray =  Gson().fromJson(playListData, Array<RadioPlayList.RadioPlayListItem>::class.java)
                    val playedList = ArrayList(playedListArray.toMutableList())

                    /**
                     * Load data on screen only if list size is greater than zero
                     */
                    if (playedList.size > 0) {
                        /**
                         * Display currently playing song details
                         */
                        val playedListData = playedList[0]
                        binding.tvSongTitle.text = playedListData.name
                        binding.tvArtistName.text = playedListData.artist
                        Glide
                            .with(this)
                            .load(playedListData.image_url)
                            .centerCrop()
                            .placeholder(R.drawable.music_placeholder)
                            .into(binding.ivAlbumArt)
                    }
                }
                is Resource.Failure -> {
                    /**
                     * Hide progressbar dialog
                     */
                    dismissProgressDialog()
                    /**
                     * Display error message
                     */
                    handleApiError(requireContext(), it)
                }
                is Resource.Loading -> {
                    /**
                     * Show Progressbar dialog
                     */
                    showProgressDialog()
                }
            }
        })

    }

    /**
     * Bind layout view
     */
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentHomeBinding.inflate(inflater, container, false)
}