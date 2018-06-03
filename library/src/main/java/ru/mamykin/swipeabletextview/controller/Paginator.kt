package ru.mamykin.swipeabletextview.controller

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

class Paginator(text: CharSequence,
                width: Int,
                height: Int,
                paint: TextPaint,
                spacingMult: Float,
                spacingAdd: Float,
                includePad: Boolean
) {

    private val pages = parsePages(text, width, height, paint, spacingMult, spacingAdd, includePad)
    var currentIndex = 0
    val pagesCount: Int = pages.size

    fun getCurrentPage(): CharSequence = pages[currentIndex]

    private fun parsePages(content: CharSequence,
                           width: Int,
                           height: Int,
                           paint: TextPaint,
                           spacingMult: Float,
                           spacingAdd: Float,
                           includePad: Boolean): List<CharSequence> {

        val alignNormal = Layout.Alignment.ALIGN_NORMAL
        val layout = StaticLayout(content, paint, width, alignNormal, spacingMult, spacingAdd, includePad)

        val lines = layout.lineCount
        val text = layout.text
        var startOffset = 0
        var bottomLineHeight = height

        val parsedPages = mutableListOf<CharSequence>()
        for (i in 0 until lines) {
            if (bottomLineHeight < layout.getLineBottom(i)) {
                val page = text.subSequence(startOffset, layout.getLineStart(i))
                parsedPages.add(page)
                startOffset = layout.getLineStart(i)
                bottomLineHeight = layout.getLineTop(i) + height
            }
        }
        val lastPage = text.subSequence(startOffset, layout.getLineEnd(lines - 1))
        return parsedPages.plus(lastPage)
    }
}