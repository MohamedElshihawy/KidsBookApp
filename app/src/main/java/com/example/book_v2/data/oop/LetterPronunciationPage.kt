package com.example.book_v2.data.oop

data class LetterPronunciationPage(
    var letterTxt: String,
    var letterPronunciation: String,
    var alternativeLetterPronunciation: String
) : Page()
