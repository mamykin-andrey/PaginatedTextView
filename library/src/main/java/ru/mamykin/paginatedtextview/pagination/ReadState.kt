package ru.mamykin.paginatedtextview.pagination

data class ReadState(
        val currentIndex: Int,
        val pagesCount: Int,
        val readPercent: Float,
        val pageText: CharSequence
)