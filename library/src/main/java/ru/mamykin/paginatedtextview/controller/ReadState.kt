package ru.mamykin.paginatedtextview.controller

data class ReadState(
        val currentIndex: Int,
        val pagesCount: Int,
        val readPercent: Float,
        val pageText: CharSequence
)