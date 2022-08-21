package ru.mamykin.paginatedtextview.view

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.AppCompatTextView
import android.text.Spannable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import ru.mamykin.paginatedtextview.extension.allWordsPositions
import ru.mamykin.paginatedtextview.pagination.PaginationController
import ru.mamykin.paginatedtextview.pagination.ReadState

/**
 * An extended TextView, which support pagination, clicks by paragraphs and long clicks by words
 */
class PaginatedTextView @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    private val textPaint = TextPaint(paint)
    private var swipeListener: OnSwipeListener? = null
    private var actionListener: OnActionListener? = null
    private lateinit var controller: PaginationController
    private var isMeasured = false

    init {
        initPaginatedTextView()
    }

    override fun scrollTo(x: Int, y: Int) {}

    /**
     * Setup the TextView
     * @param text text to set
     */
    fun setup(text: CharSequence) {
        if (isMeasured) {
            loadFirstPage(text)
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    isMeasured = true
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    loadFirstPage(text)
                }
            })
        }
    }

    /**
     * Set up the [OnActionListener]
     */
    fun setOnActionListener(listener: OnActionListener) {
        this.actionListener = listener
    }

    /**
     * Setting up a listener, which will receive swipe callbacks
     * @param [swipeListener] a listener which will receive swipe callbacks
     */
    fun setOnSwipeListener(swipeListener: OnSwipeListener) {
        this.swipeListener = swipeListener
    }

    private fun initPaginatedTextView() {
        movementMethod = SwipeableMovementMethod()
        highlightColor = Color.TRANSPARENT
    }

    private fun setPageState(pageState: ReadState) {
        this.text = pageState.pageText
        actionListener?.onPageLoaded(pageState)
        updateWordsSpannables()
    }

    private fun getSelectedWord(): String {
        return text.subSequence(selectionStart, selectionEnd).trim(' ').toString()
    }

    private fun loadFirstPage(text: CharSequence) {
        val effectWidth = width - (paddingLeft + paddingRight)
        val effectHeight = height - (paddingTop + paddingBottom)
        controller = PaginationController(
            text,
            effectWidth,
            effectHeight,
            textPaint,
            lineSpacingMultiplier,
            lineSpacingExtra
        )
        setPageState(controller.getCurrentPage())
    }

    private fun updateWordsSpannables() {
        val spans = text as Spannable
        val spaceIndexes = text.trim().allWordsPositions()
        var wordStart = 0
        var wordEnd: Int
        for (i in 0..spaceIndexes.size) {
            val swipeableSpan = createSwipeableSpan()
            wordEnd = if (i < spaceIndexes.size) spaceIndexes[i] else spans.length
            spans.setSpan(swipeableSpan, wordStart, wordEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            wordStart = wordEnd + 1
        }
    }

    private fun getSelectedParagraph(widget: TextView): String? {
        val text = widget.text
        val selStart = widget.selectionStart
        val selEnd = widget.selectionEnd
        val parStart = findLeftLineBreak(text, selStart)
        val parEnd = findRightLineBreak(text, selEnd)
        return text.subSequence(parStart, parEnd).toString()
    }

    private fun findLeftLineBreak(text: CharSequence, selStart: Int): Int {
        for (i in selStart downTo 0) {
            if (text[i] == '\n') return i + 1
        }
        return 0
    }

    private fun findRightLineBreak(text: CharSequence, selEnd: Int): Int {
        for (i in selEnd until text.length) {
            if (text[i] == '\n') return i + 1
        }
        return text.length - 1
    }

    private fun createSwipeableSpan(): SwipeableSpan = object : SwipeableSpan() {

        override fun onClick(widget: View) {
            val paragraph = getSelectedParagraph(widget as TextView)
            if (!TextUtils.isEmpty(paragraph)) {
                actionListener?.onClick(paragraph!!)
            }
        }

        override fun onLongClick(view: View) {
            val word = getSelectedWord()
            if (!TextUtils.isEmpty(word)) {
                actionListener?.onLongClick(word)
            }
        }

        override fun onSwipeLeft(view: View) {
            controller.getPrevPage()?.apply {
                setPageState(this)
                swipeListener?.onSwipeLeft()
            }
        }

        override fun onSwipeRight(view: View) {
            controller.getNextPage()?.apply {
                setPageState(this)
                swipeListener?.onSwipeRight()
            }
        }
    }
}