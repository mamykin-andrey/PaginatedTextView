package ru.mamykin.paginatedtextview.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import ru.mamykin.paginatedtextview.pagination.ReadState
import ru.mamykin.paginatedtextview.view.OnActionListener
import ru.mamykin.paginatedtextview.view.OnSwipeListener
import ru.mamykin.paginatedtextview.view.PaginatedTextView

class MainActivity : AppCompatActivity(), OnSwipeListener, OnActionListener {

    private val tag = javaClass.simpleName
    private lateinit var tvBookName: TextView
    private lateinit var tvReadPercent: TextView
    private lateinit var tvReadPages: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBookName = findViewById(R.id.tv_book_name)
        tvReadPercent = findViewById(R.id.tv_percent_read)
        tvReadPages = findViewById(R.id.tv_pages_read)

        tvBookName.text = getString(R.string.book_name)
        val tvBookContent = findViewById<PaginatedTextView>(R.id.tv_book_text)
        tvBookContent.setup(getText())
        tvBookContent.setOnActionListener(this)
        tvBookContent.setOnSwipeListener(this)
    }

    override fun onSwipeLeft() {
        Log.e(tag, "left swipe!")
    }

    override fun onSwipeRight() {
        Log.e(tag, "right swipe!")
    }

    override fun onClick(paragraph: String) {
        showToast("Paragraph clicked: $paragraph")
    }

    override fun onLongClick(word: String) {
        showToast("Word clicked: $word")
    }

    override fun onPageLoaded(state: ReadState) {
        displayReadState(state)
    }

    private fun getText(): String {
        val inputStream = resources.openRawResource(R.raw.sample_text)
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes)
        return String(bytes)
    }

    @SuppressLint("SetTextI18n")
    private fun displayReadState(readState: ReadState) {
        tvReadPages.text = "${readState.currentIndex} / ${readState.pagesCount}"
        tvReadPercent.text = "${readState.readPercent.toInt()}%"
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, LENGTH_LONG).show()
    }
}