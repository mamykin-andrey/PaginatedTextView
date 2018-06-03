package ru.mamykin.swipeabletextview.extension

fun CharSequence.allIndexesOf(char: Char): List<Int> {
    val indexes = mutableListOf<Int>()
    for (i in 0 until this.length) {
        if (this[i] == char) {
            indexes.add(i)
        }
    }
    return indexes
}