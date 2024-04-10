package com.example.itunes.states

import com.example.itunes.models.Song

sealed interface ScreenState {
    object Loading : ScreenState
    data class Ready(val songs: Array<Song>) : ScreenState {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Ready

            if (!songs.contentEquals(other.songs)) return false

            return true
        }

        override fun hashCode(): Int {
            return songs.contentHashCode()
        }
    }
    data class Error(val error: Any) : ScreenState
}