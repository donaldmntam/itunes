package com.example.itunes.models

sealed class DialogModel {
    class Error(val message: String) : DialogModel()
    class Sort(
        val sort: com.example.itunes.models.Sort,
        val onChangeSort: (com.example.itunes.models.Sort) -> Unit,
    ) : DialogModel()
}