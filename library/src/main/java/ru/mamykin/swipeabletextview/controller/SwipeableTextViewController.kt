package ru.mamykin.swipeabletextview.controller

import android.text.TextPaint

class SwipeableTextViewController(text: CharSequence,
                                  width: Int,
                                  height: Int,
                                  paint: TextPaint,
                                  spacingMult: Float,
                                  spacingAdd: Float,
                                  includePad: Boolean) {

    private val paginator = Paginator(
            text,
            width,
            height,
            paint,
            spacingMult,
            spacingAdd,
            includePad
    )

    fun getCurrentPage(): ReadState {
        return ReadState(
                paginator.currentIndex + 1,
                paginator.pagesCount,
                paginator.getCurrentPage()
        )
    }

    fun getNextPage(): ReadState {
        if (paginator.currentIndex < paginator.pagesCount - 1) {
            paginator.currentIndex++
        }
        return getCurrentPage()
    }

    fun getPrevPage(): ReadState {
        if (paginator.currentIndex > 0) {
            paginator.currentIndex--
        }
        return getCurrentPage()
    }
}