package com.example.itunes.functions

import com.example.itunes.models.Song
import com.example.itunes.models.Sort
import com.example.itunes.models.SortDirection

fun List<Song>.filter(
    songNameKeyword: String,
    albumNameKeyword: String,
): List<Song> = filter { song ->
    val songNameMatched = song.trackName.lowercase()
        .contains(songNameKeyword.lowercase())
    val albumNameMatched = song.collectionName.lowercase()
        .contains(albumNameKeyword.lowercase())
    songNameMatched && albumNameMatched
}

fun List<Song>.sort(
    sort: Sort,
    sortDirection: SortDirection,
) = sortedWith { a, b ->
    val result = when (sort) {
        is Sort.SongName -> {
            val name1 = a.trackName.lowercase()
            val name2 = b.trackName.lowercase()
            name1 compareTo name2
        }
        is Sort.AlbumName -> {
            val name1 = a.collectionName.lowercase()
            val name2 = b.collectionName.lowercase()
            name1 compareTo name2
        }
    }
    when (sortDirection) {
        is SortDirection.Ascending -> result
        is SortDirection.Descending -> -result
    }
}
