package ru.mamykin.paginatedtextview.pagination

import android.text.TextPaint

/**
 * Helper class for work with text pages
 */
class PaginationController(
        text: CharSequence,
        width: Int,
        height: Int,
        paint: TextPaint,
        spacingMult: Float,
        spacingExtra: Float
) {
    private val paginator = Paginator(text, width, height, paint, spacingMult, spacingExtra)

    /**
     * Get current page state
     * @return current page state
     */
    fun getCurrentPage() = ReadState(
            paginator.currentIndex + 1,
            paginator.pagesCount,
            getReadPercent(),
            paginator.getCurrentPage()
    )

    /**
     * Get state of next page
     * @return next page state, or null, if this page is last
     */
    fun getNextPage(): ReadState? = paginator
            .takeIf { it.currentIndex < it.pagesCount - 1 }
            ?.also { it.currentIndex++ }
            ?.let { getCurrentPage() }

    /**
     * Get state of previous page
     * @return previous page state, or null, if this page is first
     */
    fun getPrevPage(): ReadState? = paginator
            .takeIf { it.currentIndex > 0 }
            ?.also { it.currentIndex-- }
            ?.let { getCurrentPage() }

    private fun getReadPercent(): Float = when (paginator.pagesCount) {
        0 -> 0f
        else -> (paginator.currentIndex + 1) / paginator.pagesCount.toFloat() * 100
    }
}