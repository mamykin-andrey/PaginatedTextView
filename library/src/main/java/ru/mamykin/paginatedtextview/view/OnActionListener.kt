package ru.mamykin.paginatedtextview.view

import ru.mamykin.paginatedtextview.pagination.ReadState

/**
 * Interface definition for a callback to be invoked when user interacts with control
 */
interface OnActionListener {

    /**
     * Notification about displaying new page
     *
     * @param state a state of displayed page
     */
    fun onPageLoaded(state: ReadState)

    /**
     * Notification about click by paragraph
     *
     * @param paragraph paragraph clicked by the user
     */
    fun onClick(paragraph: String)

    /**
     * Notification about long click by word
     *
     * @param word word long clicked by the user
     */
    fun onLongClick(word: String)
}