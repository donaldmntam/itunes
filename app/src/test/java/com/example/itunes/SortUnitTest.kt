package com.example.itunes

import com.example.itunes.functions.sort
import com.example.itunes.models.Song
import com.example.itunes.models.Sort
import com.example.itunes.models.SortDirection
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SortUnitTest {
    private val songList = listOf(
        Song(trackName = "bcd", collectionName = "345", artworkUrl100 = "", trackViewUrl = ""),
        Song(trackName = "abc", collectionName = "123", artworkUrl100 = "", trackViewUrl = ""),
        Song(trackName = "cde", collectionName = "234", artworkUrl100 = "", trackViewUrl = ""),
    )

    @Test
    fun `when user sorts by song name in ascending order`() {
        val actual = songList.sort(
            sort = Sort.SongName,
            sortDirection = SortDirection.Ascending,
        )
        val expected = listOf(
            Song(trackName = "abc", collectionName = "123", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "bcd", collectionName = "345", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "cde", collectionName = "234", artworkUrl100 = "", trackViewUrl = ""),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `when user sorts by song name in descending order`() {
        val actual = songList.sort(
            sort = Sort.SongName,
            sortDirection = SortDirection.Descending,
        )
        val expected = listOf(
            Song(trackName = "cde", collectionName = "234", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "bcd", collectionName = "345", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "abc", collectionName = "123", artworkUrl100 = "", trackViewUrl = ""),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `when user sorts by album name in ascending order`() {
        val actual = songList.sort(
            sort = Sort.AlbumName,
            sortDirection = SortDirection.Ascending,
        )
        val expected = listOf(
            Song(trackName = "abc", collectionName = "123", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "cde", collectionName = "234", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "bcd", collectionName = "345", artworkUrl100 = "", trackViewUrl = ""),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `when user sorts by album name in descending order`() {
        val actual = songList.sort(
            sort = Sort.AlbumName,
            sortDirection = SortDirection.Descending,
        )
        val expected = listOf(
            Song(trackName = "bcd", collectionName = "345", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "cde", collectionName = "234", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "abc", collectionName = "123", artworkUrl100 = "", trackViewUrl = ""),
        )
        assertEquals(expected, actual)
    }
}