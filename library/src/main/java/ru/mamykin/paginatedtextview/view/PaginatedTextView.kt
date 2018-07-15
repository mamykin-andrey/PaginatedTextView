package ru.mamykin.paginatedtextview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.RectF
import android.support.v7.widget.AppCompatTextView
import android.text.*
import android.util.AttributeSet
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import ru.mamykin.paginatedtextview.extension.allWordsPositions
import ru.mamykin.paginatedtextview.extension.getMaxLineWidth
import ru.mamykin.paginatedtextview.pagination.PaginationController
import ru.mamykin.paginatedtextview.pagination.ReadState

/**
 * An extended TextView, which support pagination, clicks by paragraphs and long clicks by words
 */
class PaginatedTextView : AppCompatTextView {

    companion object {
        const val MIN_TEXT_SIZE = 26
        const val DEFAULT_SPACING_MULT = 1.0f
        const val DEFAULT_SPACING_ADD = 0.0f
    }

    private val textCachedSizes = SparseIntArray()
    private val availableSpaceRect = RectF()
    private var maxTextSize = textSize
    private val textRect = RectF()
    private val textPaint = TextPaint(paint)
    private var initializedDimens: Boolean = false
    private var swipeListener: OnSwipeListener? = null
    private var actionListener: OnActionListener? = null
    private lateinit var controller: PaginationController

    constructor(context: Context) : super(context) {
        initPaginatedTextView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initPaginatedTextView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) :
            super(context, attrs, defStyle) {
        initPaginatedTextView()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        initializedDimens = true
        textCachedSizes.clear()
        if (width != oldWidth || height != oldHeight) {
            adjustTextSize()
        }
    }

    override fun setTextSize(size: Float) {
        maxTextSize = size
        textCachedSizes.clear()
        adjustTextSize()
    }

    override fun setTextSize(unit: Int, size: Float) {
        val resources = context?.resources ?: Resources.getSystem()
        maxTextSize = TypedValue.applyDimension(unit, size, resources.displayMetrics)
        textCachedSizes.clear()
        adjustTextSize()
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        adjustTextSize()
    }

    /**
     * Setting up a TextView
     *
     * @param text text to set
     */
    fun setup(text: CharSequence) {
        val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                loadFirstPage(text)
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }

    /**
     * Setting up a listener, which will receive following callbacks:
     * - about page loading
     * - about clicks by paragraphs
     * - about long clicks by words
     *
     * @param listener a listener, which will receive callbacks
     */
    fun setOnActionListener(listener: OnActionListener) {
        this.actionListener = listener
    }

    /**
     * Setting up a listener, which will receive swipe callbacks
     *
     * @param listener a listener which will receive swipe callbacks
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
        controller = PaginationController(
                text,
                width,
                height,
                textPaint,
                lineSpacingMultiplier,
                lineSpacingExtra,
                includeFontPadding
        )
        setPageState(controller.getCurrentPage())
    }

    private fun adjustTextSize() {
        if (initializedDimens) {
            val heightLimit = measuredHeight - compoundPaddingBottom - compoundPaddingTop
            val widthLimit = measuredWidth - compoundPaddingLeft - compoundPaddingRight
            availableSpaceRect.right = widthLimit.toFloat()
            availableSpaceRect.bottom = heightLimit.toFloat()
            val textSize = calculateFitsTextSize(
                    MIN_TEXT_SIZE,
                    maxTextSize.toInt(),
                    availableSpaceRect,
                    widthLimit
            )
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        }
    }

    private fun checkTextFits(suggestedSize: Int, availableRect: RectF, widthLimit: Int): Boolean {
        textPaint.textSize = suggestedSize.toFloat()
        val layout = StaticLayout(
                text,
                textPaint,
                widthLimit,
                Layout.Alignment.ALIGN_NORMAL,
                DEFAULT_SPACING_MULT,
                DEFAULT_SPACING_ADD,
                true
        )
        textRect.bottom = layout.height.toFloat()
        textRect.right = layout.getMaxLineWidth()
        return containsRectF(availableRect, textRect)
    }

    private fun containsRectF(containerRect: RectF, actualRect: RectF): Boolean {
        containerRect.offset(0f, 0f)
        val aArea = containerRect.width() * containerRect.height()
        actualRect.offset(0f, 0f)
        val bArea = actualRect.width() * actualRect.height()
        return aArea >= bArea
    }

    private fun calculateFitsTextSize(start: Int,
                                      end: Int,
                                      availableSpace: RectF,
                                      widthLimit: Int): Int {
        val key = text.length
        val size = textCachedSizes.get(key)
        return if (size != 0)
            size
        else textSizeSearch(start, end, availableSpace, widthLimit).apply {
            textCachedSizes.put(key, this)
        }
    }

    private fun textSizeSearch(minSize: Int,
                               maxSize: Int,
                               availableSpace: RectF,
                               widthLimit: Int): Int {
        for (size in maxSize downTo minSize) {
            if (checkTextFits(size, availableSpace, widthLimit)) {
                return size
            }
        }
        return minSize
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