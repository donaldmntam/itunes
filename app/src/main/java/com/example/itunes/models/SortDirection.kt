package com.example.itunes.models

sealed class SortDirection {
    object Ascending : SortDirection()
    object Descending : SortDirection()
}
