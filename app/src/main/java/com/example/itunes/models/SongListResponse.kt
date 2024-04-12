package com.example.itunes.models

data class SongListResponse(
    val resultCount: Int,
    val results: List<Song>,
)
