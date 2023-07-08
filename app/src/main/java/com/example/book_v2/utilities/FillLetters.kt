package com.example.book_v2.utilities

import android.graphics.Color
import android.util.Range
import com.example.book_v2.data.models.Circle

class FillLetters {
    fun fillCircleOnTouch(x: Float, y: Float, circleList: List<Circle>?, position: Int): Boolean {
        val r: Int
        val xStart: Float
        val xEnd: Float
        val yStart: Float
        val yEnd: Float
        circleList?.let {
            if (position < circleList.size) {
                val nextCircleX = circleList[position].x.toFloat()
                val nextCircleY = circleList[position].y.toFloat()
                r = circleList[position].radius
                xStart = nextCircleX - r
                xEnd = nextCircleX + r
                yStart = nextCircleY - r
                yEnd = nextCircleY + r
                val xRange = Range(xStart, xEnd)
                val yRange = Range(yStart, yEnd)
                if (xRange.contains(x) && yRange.contains(y)) {
                    return true
                }
            }
        }
        return false
    }

    fun blendColors(color: String?, color0: String?, ratio: Float): Int {
        val inverseRation = 1f - ratio
        val color1 = Color.parseColor(color)
        val color2 = Color.parseColor(color0)
        val r = Color.red(color1) * ratio + Color.red(color2) * inverseRation
        val g = Color.green(color1) * ratio + Color.green(color2) * inverseRation
        val b = Color.blue(color1) * ratio + Color.blue(color2) * inverseRation
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }
}
