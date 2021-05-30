package com.intertech.radioplayer.ui.base

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : MultiDexApplication() {
    companion object {
        var context: BaseApplication? = null

        @Synchronized
        fun getInstance(): MultiDexApplication? {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}