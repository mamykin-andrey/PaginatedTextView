package ru.mamykin.swipeabletextview.view

import android.text.style.ClickableSpan
import android.view.View

abstract class LongClickableSpan : ClickableSpan() {

    abstract fun onLongClick(view: View)
}