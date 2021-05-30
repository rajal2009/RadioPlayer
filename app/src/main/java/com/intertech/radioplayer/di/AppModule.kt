package com.intertech.radioplayer.di

import com.intertech.radioplayer.BuildConfig
import com.intertech.radioplayer.data.network.ApiService
import com.intertech.radioplayer.data.repository.PlayListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRadioPlayListRepository(
        api: ApiService
    ) = PlayListRepository(api)

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }
}