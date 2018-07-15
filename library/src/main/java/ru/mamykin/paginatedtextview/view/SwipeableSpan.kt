package ru.mamykin.paginatedtextview.view

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * An extended version of ClickableSpan, which also support long clicks and swipes
 */
abstract class SwipeableSpan : ClickableSpan() {

    /**
     * Long click event
     *
     * @param view View, which rise the event
     */
    abstract fun onLongClick(view: View)

    /**
     * Swipe from right to left
     *
     * @param view View, which rise the event
     */
    abstract fun onSwipeLeft(view: View)

    /**
     * Swipe from left to right
     *
     * @param view View, which rise the event
     */
    abstract fun onSwipeRight(view: View)

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        ds?.isUnderlineText = false
        ds?.color = Color.BLACK
    }
}