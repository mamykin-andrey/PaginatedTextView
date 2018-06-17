package ru.mamykin.paginatedtextview.view

import ru.mamykin.paginatedtextview.pagination.ReadState

interface OnActionListener {

    fun onPageLoaded(state: ReadState)

    fun onClick(paragraph: String)

    fun onLongClick(word: String)
}