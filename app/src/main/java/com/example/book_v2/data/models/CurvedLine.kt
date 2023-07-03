package com.example.book_v2.data.models

import android.graphics.Path

data class CurvedLine(
    var x1: Int?,
    var y1: Int?,
    var x2: Int?,
    var y2: Int?,
    var radius: Int?,
    var linePath: Path?
) {
    constructor() : this(0, 0, 0, 0, 0, null)
}