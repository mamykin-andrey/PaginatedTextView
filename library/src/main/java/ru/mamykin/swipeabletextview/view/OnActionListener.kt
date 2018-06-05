package ru.mamykin.swipeabletextview.view

import ru.mamykin.swipeabletextview.controller.ReadState

interface OnActionListener {

    fun onPageLoaded(state: ReadState)

    fun onClick(paragraph: String)

    fun onLongClick(word: String)
}