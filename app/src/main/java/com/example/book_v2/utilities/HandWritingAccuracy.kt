package com.example.book_v2.utilities

import android.graphics.Point
import android.util.Log
import com.example.book_v2.data.models.Circle
import com.example.book_v2.data.oop.WritingScore
import com.example.book_v2.services.interfaces.TaskCompletionListener
import kotlin.math.pow
import kotlin.math.sqrt

class HandWritingAccuracy(private val listener: TaskCompletionListener) {
    private var currentPoint = 0
    private var accuracyScore = 0.0
    private val perfectScore = 1
    private val halfScore = .5
    private val quarterScore = .25
    var fullPath = ArrayList<Point>()
    var radius = 0
    var space = 0

    fun calculateRightPath(circleList: List<Circle>) {
        val xPoints = ArrayList<Int>()
        val yPoints = ArrayList<Int>()
        circleList.forEach { element ->
            xPoints.add(element.x)
            yPoints.add(element.y)
        }
        radius = circleList[0].radius

        for (i in 0 until xPoints.size-1) {

            val xStart = xPoints[i]
            val xEnd = xPoints[i + 1]
            val yStart = yPoints[i]
            val yEnd = yPoints[i + 1]
            val spaceBetweenTwoPoints = sqrt(
                (xEnd - xStart).toDouble().pow(2.0) + (yEnd - yStart).toDouble().pow(2.0)
            )
            if (spaceBetweenTwoPoints >= (2 * radius) + (space + 25)) {
                fullPath.add(Point(xStart, yStart))
            } else {
                fullPath.add(Point(xStart, yStart))
                var distanceRatio = .2
                while (distanceRatio <= 1) {
                    val xOfNextPoint = (1 - distanceRatio) * xStart + distanceRatio * xEnd
                    val yOfNextPoint = (1 - distanceRatio) * yStart + distanceRatio * yEnd
                    fullPath.add(Point(xOfNextPoint.toInt(), yOfNextPoint.toInt()))
                    distanceRatio += .2
                }
            }
        }
        fullPath.add(Point(xPoints.last(), yPoints.last()))
    }



    fun observeUserHandWriting(x: Int, y: Int): WritingScore {
        if (currentPoint < fullPath.size) {
            val distanceBetweenPointAndPath = sqrt(
                (x - fullPath[currentPoint].x).toDouble().pow(2.0)
                        + (y - fullPath[currentPoint].y).toDouble().pow(2.0)
            )
            if (distanceBetweenPointAndPath < (.3 * radius)) {
                currentPoint++
                accuracyScore += perfectScore
                listener.onProgressAchieved(accuracyScore, fullPath.size)
                return WritingScore.Optimal
            } else {
                val distanceWithPreviousPoint = if (currentPoint > 0) {
                    sqrt(
                        (x - fullPath[currentPoint - 1].x).toDouble().pow(2.0)
                                + (y - fullPath[currentPoint - 1].y).toDouble().pow(2.0)
                    )
                } else {
                    sqrt(
                        (x - fullPath[0].x).toDouble().pow(2.0)
                                + (y - fullPath[0].y).toDouble().pow(2.0)
                    )
                }
                if (distanceWithPreviousPoint < radius) {
                } else if (distanceBetweenPointAndPath > (.3 * radius)
                    && distanceBetweenPointAndPath < (radius)
                ) {
                    currentPoint++
                    accuracyScore += halfScore
                    listener.onProgressAchieved(accuracyScore, fullPath.size)
                    return WritingScore.HalfOptimal
                } else if (distanceBetweenPointAndPath > (radius)
                    && distanceBetweenPointAndPath <= (2 * radius)
                ) {
                    currentPoint++
                    accuracyScore += quarterScore
                    listener.onProgressAchieved(accuracyScore, fullPath.size)
                    return WritingScore.QuarterOptimal
                } else if (distanceBetweenPointAndPath > 2 * radius) {
                    accuracyScore -= halfScore
                    listener.onOutOfPathLineDetected(x.toFloat(), y.toFloat() ,accuracyScore)
                    return WritingScore.OutOfRange
                }
            }
        } else {
            listener.onTaskCompleted(accuracyScore)
        }


        return WritingScore.Continue
    }


    fun resetAccuracyValue() {
        accuracyScore = 0.0
        currentPoint = 0
    }


}