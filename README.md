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

```gradle
implementation 'ru.mamykin.widget:paginatedtextview:0.1.0'
```

**2)**  Add `ru.mamykin.paginatedtextview.view.PaginatedTextView` to your layout.
Init control with your text:

```kotlin
val tvBookContent = findViewById<PaginatedTextView>(R.id.tv_book_text)
tvBookContent.setup(getText())
```

**3)**  Set OnActionListener and OnSwipeListener for detecting paragraph/words clicks and left/right swipes.

```kotlin
class MyActivity : AppCompatActivity(), OnSwipeListener, OnActionListener {

  private fun init() {
    ...
    tvBookContent.setOnActionListener(this)
    tvBookContent.setOnSwipeListener(this)
    ...
  }
  
  override fun onSwipeLeft() {} // Called when user swiped left
  
  override fun onSwipeRight() {} // Called when user swiped right
  
  override fun onClick(paragraph: String) {} // Called when user clicked by a paragraph
  
  override fun onLongClick(word: String) {} // Called when user long clicked by a word
  
  override fun onPageLoaded(state: ReadState) {} // Called after page loading
```

For more details see [sample](https://github.com/mamykin-av/PaginatedTextView/tree/master/sample) module
