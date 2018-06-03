package ru.mamykin.swipeabletextview.view

import android.text.style.ClickableSpan
import android.view.View

abstract class SwipeableSpan : ClickableSpan() {

    abstract fun onLongClick(view: View)
}