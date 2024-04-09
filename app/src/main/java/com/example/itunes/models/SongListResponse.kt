package com.example.itunes.models

data class SongListResponse(
    val resultCount: Int,
    val results: Array<Song>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongListResponse

        if (resultCount != other.resultCount) return false
        if (!results.contentEquals(other.results)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resultCount
        result = 31 * result + results.contentHashCode()
        return result
    }
}
