package ru.mamykin.swipeabletextview

import android.text.style.ClickableSpan
import android.view.View

/**
 * Класс для расширения Span лонг тапом и горизонтальными свайпами
 */
abstract class SwipeableSpan : ClickableSpan() {

    abstract fun onLongClick(view: View)
}