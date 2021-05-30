package com.intertech.radioplayer.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intertech.radioplayer.data.repository.BaseRepository
import com.intertech.radioplayer.data.repository.PlayListRepository
import com.intertech.radioplayer.ui.main.PlayListViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            // bind view model classes
            modelClass.isAssignableFrom(PlayListViewModel::class.java) -> PlayListViewModel(
                repository as PlayListRepository
            ) as T

            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}