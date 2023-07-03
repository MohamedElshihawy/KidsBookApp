package com.example.book_v2.data.oop

import com.example.book_v2.data.models.Circle


data class ConstrainedMatchingPage(
    var numOfPatterns: Short = 0,
    var patternsPaths: List<String> =ArrayList(),
    var patternsRepresentation: MutableList<List<Circle>> = ArrayList()
) : Page()