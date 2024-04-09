package com.example.itunes.services

import com.example.itunes.models.SongListResponse

interface SongListProvider {
    suspend fun fetch(): Result<SongListResponse>
}