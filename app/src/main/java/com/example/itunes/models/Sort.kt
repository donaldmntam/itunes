package com.example.itunes.models

sealed class Sort {
    object SongName : Sort()
    object AlbumName : Sort()
}
