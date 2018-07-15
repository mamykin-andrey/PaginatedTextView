package ru.mamykin.paginatedtextview.extension

fun CharSequence.allWordsPositions(): List<Int> {
    val indexes = mutableListOf<Int>()
    for (i in 0 until this.length) {
        if (this[i] == ' ' || this[i] == '\n') {
            indexes.add(i)
        }
    }
    return indexes
}