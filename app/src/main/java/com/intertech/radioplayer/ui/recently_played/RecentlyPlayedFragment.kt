package com.intertech.radioplayer.ui.recently_played

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.intertech.radioplayer.adapter.RecentlyPlayedListAdapter
import com.intertech.radioplayer.data.model.RadioPlayList
import com.intertech.radioplayer.data.network.Resource
import com.intertech.radioplayer.data.network.handleApiError
import com.intertech.radioplayer.databinding.FragmentRecentlyPlayedBinding
import com.intertech.radioplayer.ui.base.BaseFragment
import com.intertech.radioplayer.ui.main.PlayListViewModel
import kotlinx.android.synthetic.main.fragment_recently_played.*

class RecentlyPlayedFragment :  BaseFragment<FragmentRecentlyPlayedBinding>()  {

    /**
     * Get activity view model
     */
    private val fragmentViewModel: PlayListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                         * Removed currently playing song from list
                         */
                        playedList.removeAt(0)

                       // 3. The “Recently Played” view will display a list of the last 10 songs played on the radio stream. 
                        playedList.take(10)

                        /**
                         * Display list of data into recyclerview
                         */
                        binding.rvRecentlyPlay.also { recyclerView ->
                            // Add layout manager
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                            recyclerView.hasFixedSize()

                            // Initialize adapter and add to recyclerview
                            recyclerView.adapter =
                                RecentlyPlayedListAdapter(requireContext(), playedList)

                            // Add divider line between item to separate item view
                            recyclerView.addItemDecoration(
                                DividerItemDecoration(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL
                                )
                            )
                        }
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
    )= FragmentRecentlyPlayedBinding.inflate(inflater, container, false)
}