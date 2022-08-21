package ru.mamykin.paginatedtextview.pagination

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

/**
 * A class, which split text by pages, and control pages switching
 */
class Paginator(
    text: CharSequence,
    width: Int,
    height: Int,
    paint: TextPaint,
    spacingMult: Float,
    spacingAdd: Float
) {
    private val pages = parsePages(
        text,
        width,
        height,
        paint,
        spacingMult,
        spacingAdd
    )
    var currentIndex = 0
    val pagesCount = pages.size

    /**
     * Get current page text
     */
    fun getCurrentPage(): CharSequence = pages[currentIndex]

    private fun parsePages(
        content: CharSequence,
        width: Int,
        height: Int,
        paint: TextPaint,
        spacingMult: Float,
        spacingAdd: Float
    ): List<CharSequence> {
        val layout = StaticLayout(
            content,
            paint,
            width,
            Layout.Alignment.ALIGN_NORMAL,
            spacingMult,
            spacingAdd,
            true
        )

        val text = layout.text
        var startOffset = 0
        var lastBottomLineHeight = height

        val parsedPages = mutableListOf<CharSequence>()
        for (i in 0 until layout.lineCount) {
            if (lastBottomLineHeight < layout.getLineBottom(i)) {
                val page = text.subSequence(startOffset, layout.getLineStart(i))
                parsedPages.add(page)
                startOffset = layout.getLineStart(i)
                lastBottomLineHeight = layout.getLineTop(i) + height
            }
        }
        val lastPage = text.subSequence(startOffset, layout.getLineEnd(layout.lineCount - 1))
        return parsedPages.plus(lastPage)
    }
}