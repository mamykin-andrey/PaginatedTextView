package ru.mamykin.paginatedtextview.view

import android.text.style.ClickableSpan
import android.view.View

abstract class SwipeableSpan : ClickableSpan() {

    abstract fun onLongClick(view: View)

    abstract fun onSwipeLeft(view: View)

    abstract fun onSwipeRight(view: View)
}