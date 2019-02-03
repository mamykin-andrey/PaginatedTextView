# PaginatedTextView

### [](https://github.com/mamykin-av/PaginatedTextView/#description)Description
Extended Android TextView with pagination and clicks handling

### [](https://github.com/mamykin-av/PaginatedTextView#features)Features

-   Page pagination
-   Page fliping over left/right swipes
-   Detecting clicks by paragraphs
-   Detecting long clicks by words

<img src="/screens/word_clicked.jpg" width="250" />    <img src="/screens/paragraph_clicked.jpg" width="250" />    <img src="/screens/second_page.jpg" width="250" />

### [](https://github.com/mamykin-av/PaginatedTextView#integration)Integration

**1)**  Add library as a dependency to your project

`implementation 'ru.mamykin.widget:paginatedtextview:0.1.0'`

**2)**  Add `ru.mamykin.paginatedtextview.view.PaginatedTextView` to your layout.
Init control with your text:

`val tvBookContent = findViewById<PaginatedTextView>(R.id.tv_book_text)`

`tvBookContent.setup(getText())`

**3)**  Set OnActionListener and OnSwipeListener for detecting paragraph/words clicks and left/right swipes.

`class MyActivity : AppCompatActivity(), OnSwipeListener, OnActionListener {`

`tvBookContent.setOnActionListener(this)`

`tvBookContent.setOnSwipeListener(this)`

`override fun onSwipeLeft() {}` // Called when user was swiped left

`override fun onSwipeRight() {}` // Called when user was swiped right

`override fun onClick(paragraph: String) {}` // Called when use was clicked by paragraph

`override fun onLongClick(word: String) {}` // Called when user was long clicked by word

`override fun onPageLoaded(state: ReadState) {}` // Called when page was loaded

...

For more details use `sample` application as example

Let me know if I missed something, appreciate your support, thanks!
