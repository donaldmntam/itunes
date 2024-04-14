package com.example.itunes

import com.example.itunes.functions.filter
import com.example.itunes.models.Song
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FilterUnitTest {
    private val songList = listOf(
        Song(trackName = "Love", collectionName = "Heart", artworkUrl100 = "", trackViewUrl = ""),
        Song(trackName = "123", collectionName = "Untitled", artworkUrl100 = "", trackViewUrl = ""),
        Song(trackName = "Lemon Elegy", collectionName = "Empty Promises", artworkUrl100 = "", trackViewUrl = ""),
    )

    @Test
    fun `when user does not filter`() {
        val actual = songList.filter(
            songNameKeyword = "",
            albumNameKeyword = "",
        )
        val expected = songList
        assertEquals(expected, actual)
    }

    @Test
    fun `when user only filters with an album name`() {
        val actual = songList.filter(
            songNameKeyword = "",
            albumNameKeyword = "promi",
        )
        val expected = listOf(
            Song(trackName = "Lemon Elegy", collectionName = "Empty Promises", artworkUrl100 = "", trackViewUrl = ""),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `when user only filters with a song name`() {
        val actual = songList.filter(
            songNameKeyword = "e",
            albumNameKeyword = "",
        )
        val expected = listOf(
            Song(trackName = "Love", collectionName = "Heart", artworkUrl100 = "", trackViewUrl = ""),
            Song(trackName = "Lemon Elegy", collectionName = "Empty Promises", artworkUrl100 = "", trackViewUrl = ""),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `when user only filters with a song name and an album name`() {
        val actual = songList.filter(
            songNameKeyword = "3",
            albumNameKeyword = "titled",
        )
        val expected = listOf(
            Song(trackName = "123", collectionName = "Untitled", artworkUrl100 = "", trackViewUrl = ""),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `when user filters with a non-existent song name`() {
        val actual = songList.filter(
            songNameKeyword = "qwerty",
            albumNameKeyword = "",
        )
        val expected = emptyList<Song>()
        assertEquals(expected, actual)
    }

    @Test
    fun `when user filters with a non-existent album name`() {
        val actual = songList.filter(
            songNameKeyword = "",
            albumNameKeyword = "123456",
        )
        val expected = emptyList<Song>()
        assertEquals(expected, actual)
    }
}