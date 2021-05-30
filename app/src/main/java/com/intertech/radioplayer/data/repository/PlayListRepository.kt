package com.intertech.radioplayer.data.repository

import com.intertech.radioplayer.data.network.ApiService
import javax.inject.Inject

class PlayListRepository @Inject constructor(
    private val api: ApiService
) : BaseRepository() {

    // Make Radio play list api call
    suspend fun getRadioPlayList() = safeApiCall {
        api.radioPlayList()
    }
}