package ru.mamykin.paginatedtextview.pagination

import android.text.TextPaint

/**
 * Helper class for work with text pages
 */
class PaginationController(text: CharSequence,
                           width: Int,
                           height: Int,
                           paint: TextPaint,
                           spacingMult: Float,
                           spacingAdd: Float,
                           includePad: Boolean) {

    private val paginator = Paginator(text, width, height, paint, spacingMult, spacingAdd, includePad)

    /**
     * Get current page state
     *
     * @return current page state
     */
    fun getCurrentPage(): ReadState {
        return ReadState(paginator.currentIndex + 1,
                paginator.pagesCount,
                getReadPercent(),
                paginator.getCurrentPage()
        )
    }

    /**
     * Get state of next page
     *
     * @return next page state, or null, if this page is last
     */
    fun getNextPage(): ReadState? {
        return if (paginator.currentIndex < paginator.pagesCount - 1) {
            paginator.currentIndex++
            getCurrentPage()
        } else {
            null
        }
    }

    /**
     * Get state of previous page
     *
     * @return previous page state, or null, if this page is first
     */
    fun getPrevPage(): ReadState? {
        return if (paginator.currentIndex > 0) {
            paginator.currentIndex--
            getCurrentPage()
        } else {
            null
        }
    }

    private fun getReadPercent(): Float = when (paginator.pagesCount) {
        0 -> 0f
        else -> (paginator.currentIndex + 1) / paginator.pagesCount.toFloat() * 100
    }
}