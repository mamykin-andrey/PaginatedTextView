package ru.mamykin.swipeabletextview

interface OnActionListener {

    fun onClick(paragraph: String)

    fun onLongClick(word: String)
}