package com.intertech.radioplayer.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intertech.radioplayer.data.model.RadioPlayList
import com.intertech.radioplayer.data.network.Resource
import com.intertech.radioplayer.data.repository.PlayListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//7. Code architecture should be MVVM. 
@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val repository: PlayListRepository
) : ViewModel() {

    // Store updated data
    private val _playListResponse: MutableLiveData<Resource<RadioPlayList>> = MutableLiveData()

    val playListResponse: LiveData<Resource<RadioPlayList>>
        get() = _playListResponse

    /**
     * Receive api response from repository and update mutable live data object value
     */
    fun playList() = viewModelScope.launch {
        _playListResponse.value = Resource.Loading
        _playListResponse.value = repository.getRadioPlayList()
    }
}