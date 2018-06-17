package ru.mamykin.paginatedtextview.view

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

abstract class SwipeableSpan : ClickableSpan() {

    abstract fun onLongClick(view: View)

    abstract fun onSwipeLeft(view: View)

    abstract fun onSwipeRight(view: View)

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        ds?.isUnderlineText = false
        ds?.color = Color.BLACK
    }
}