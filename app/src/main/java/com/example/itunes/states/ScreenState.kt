package com.example.itunes.states

import com.example.itunes.models.Song

sealed interface ScreenState {
    object Loading : ScreenState
    data class Ready(val songs: List<Song>) : ScreenState
    data class Error(val error: Any) : ScreenState
}