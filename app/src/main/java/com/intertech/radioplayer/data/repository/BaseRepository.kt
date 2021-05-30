package com.intertech.radioplayer.data.repository

import com.intertech.radioplayer.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                // Pass response to Success response callback
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                // Pass error code to Error Response callback
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }

                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }
}