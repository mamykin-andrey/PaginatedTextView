package ru.mamykin.paginatedtextview.view

import android.os.Handler
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

class SwipeableMovementMethod : LinkMovementMethod() {

    companion object {
        const val MIN_TIME_THRESHOLD = 50
        const val MAX_TIME_THRESHOLD = 500
        const val MIN_COORD_POSITIVE_THRESHOLD = 100
        const val MIN_COORD_NEGATIVE_THRESHOLD = MIN_COORD_POSITIVE_THRESHOLD * -1
    }

    private var longClickHandler = Handler()
    private var startXCoord: Double = 0.0
    private var startTime: Long = 0

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_CANCEL -> handleCancelAction()
            MotionEvent.ACTION_DOWN -> handleDownAction(event, buffer, widget)
            MotionEvent.ACTION_UP -> handleUpAction(event, buffer, widget)
        }
        return true
    }

    private fun handleCancelAction() {
        longClickHandler.removeCallbacksAndMessages(null)
    }

    private fun handleDownAction(event: MotionEvent, buffer: Spannable, widget: TextView) {
        startXCoord = event.x.toDouble()
        startTime = event.eventTime

        val link = getClickableSpan(event, widget, buffer)
        Selection.setSelection(buffer, buffer.getSpanStart(link), buffer.getSpanEnd(link))

        longClickHandler.postDelayed({ link.onLongClick(widget) }, 1000)
    }

    private fun handleUpAction(event: MotionEvent, buffer: Spannable, widget: TextView) {
        val timeDiff = event.eventTime - startTime
        val xCoordDiff = event.x - startXCoord

        if (timeDiff in MIN_TIME_THRESHOLD..MAX_TIME_THRESHOLD) {
            longClickHandler.removeCallbacksAndMessages(null)
            val link = getClickableSpan(event, widget, buffer)
            when {
                xCoordDiff > MIN_COORD_POSITIVE_THRESHOLD -> link.onSwipeLeft(widget)
                xCoordDiff < MIN_COORD_NEGATIVE_THRESHOLD -> link.onSwipeRight(widget)
                else -> link.onClick(widget)
            }
        }
    }

    private fun getClickableSpan(event: MotionEvent,
                                 widget: TextView,
                                 buffer: Spannable): SwipeableSpan {

        val clickX = event.x - widget.totalPaddingLeft + widget.scrollX
        val clickY = event.y.toInt() - widget.totalPaddingTop + widget.scrollY

        val line = widget.layout.getLineForVertical(clickY)
        val offset = widget.layout.getOffsetForHorizontal(line, clickX)

        val spans = buffer.getSpans(offset, offset, SwipeableSpan::class.java)
        return spans[0]
    }
}