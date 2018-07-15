package ru.mamykin.paginatedtextview.extension

import android.text.StaticLayout

fun StaticLayout.getMaxLineWidth(): Float {
    var maxLineWidth = -1f
    for (i in 0 until this.lineCount) {
        val curLineWidth = this.getLineWidth(i)
        if (curLineWidth > maxLineWidth) {
            maxLineWidth = curLineWidth
        }
    }
    return maxLineWidth
}