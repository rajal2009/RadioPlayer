package com.intertech.radioplayer.data.network

import okhttp3.ResponseBody

sealed class Resource<out T> {
    // Success callback
    data class Success<out T>(val value: T) : Resource<T>()

    // Failure callback
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()

    // Handle loader state
    object Loading : Resource<Nothing>()

}