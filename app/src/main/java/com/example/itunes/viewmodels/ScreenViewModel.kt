package com.example.itunes.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.itunes.models.SongListResponse
import com.example.itunes.services.SongListProvider
import com.example.itunes.states.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScreenViewModel(
    private val songListProvider: SongListProvider
) : ViewModel() {
    private val _state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
    val state: StateFlow<ScreenState> = _state.asStateFlow()

    init {
        Log.d("donald", "init inside screen view model here")
        viewModelScope.launch {
            Log.d("donald", "fetching song list here")
            val result = songListProvider.fetch()
            result
                .onSuccess(::didFetchSongList)
                .onFailure(::didReceivedError)
        }
    }

    fun reload() {
        val current = _state.value
        if (current !is ScreenState.Error) return
        viewModelScope.launch {
            _state.emit(ScreenState.Loading)
            val result = songListProvider.fetch()
            result
                .onSuccess(::didFetchSongList)
                .onFailure(::didReceivedError)
        }
    }

    private fun didFetchSongList(response: SongListResponse) {
        val current = _state.value
        if (current !is ScreenState.Loading) return
        viewModelScope.launch {
            _state.emit(ScreenState.Ready(response.results))
        }
    }

    private fun didReceivedError(error: Any) {
        Log.d("donald", "did receive error here donald")
        val current = _state.value
        if (current !is ScreenState.Loading) return
        viewModelScope.launch {
            _state.emit(ScreenState.Error(error))
        }
    }
}

class ScreenViewModelFactory(
    private val songListProvider: SongListProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScreenViewModel(songListProvider) as T
    }
}
