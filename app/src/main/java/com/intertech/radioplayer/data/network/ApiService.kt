package com.intertech.radioplayer.data.network


import com.intertech.radioplayer.data.model.RadioPlayList
import retrofit2.http.GET


interface ApiService {

    @GET("testapi")
    suspend fun radioPlayList(): RadioPlayList
}