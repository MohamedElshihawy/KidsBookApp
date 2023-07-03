package com.example.book_v2.data.oop


data class ColoringPage(
    var filledImagePath: String ="",
    var outlinedImagePath: String = "",
    var coloredImagePath: String = "",
) : Page() {
}