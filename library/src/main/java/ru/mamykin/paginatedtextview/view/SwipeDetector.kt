package ru.mamykin.paginatedtextview.view

import android.view.MotionEvent
import android.view.View

class SwipeDetector(private val listener: OnSwipeListener) : View.OnTouchListener {

    companion object {
        const val MIN_TIME_THRESHOLD = 1000
        const val MIN_COORD_THRESHOLD = 100
    }

    private var startXCoord: Float = 0f
    private var startTime: Long = 0

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> handleDownAction(event)
            MotionEvent.ACTION_UP -> handleUpAction(event)
        }
        return true
    }

    private fun handleDownAction(event: MotionEvent) {
        startXCoord = event.x
        startTime = event.eventTime
    }

    private fun handleUpAction(event: MotionEvent) {
        val diffTime = event.eventTime - startTime
        val diffXCoord = event.x - startXCoord
        if (diffTime < MIN_TIME_THRESHOLD && Math.abs(diffXCoord) > MIN_COORD_THRESHOLD) {
            if (diffXCoord > 0) {
                listener.onSwipeLeft()
            } else {
                listener.onSwipeRight()
            }
        }
    }
}