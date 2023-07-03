package com.example.book_v2.utilities

import android.content.ContentValues
import android.graphics.Point
import android.util.Log
import android.util.Range
import com.example.book_v2.data.models.Circle
import com.example.book_v2.services.interfaces.TaskCompletionListener
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class CalculateLetterAccuracy {
    private var spaceBetweenPoints = 0
    private var radiusOfPoint = 0

    private var rightPath = ArrayList<Point>()
    private var wrongPath = ArrayList<Point>()
    var isPathFinished = false
    var rightFullPath: ArrayList<Point> = ArrayList()
    private var previousPoint = Point()


    private fun accuracyPercentage(): Int {
        Log.i("TAG", "accuracyPercentage: right " + rightPath.size)
        val size = (rightPath.size + wrongPath.size).toDouble()
        Log.i("TAG", "accuracyPercentage: wrong " + wrongPath.size)
        Log.i("TAG", "accuracyPercentage: all $size")
        val s = rightPath.size.toDouble() / size * 100
        //   Log.e(TAG, " Accuracy: " + s);
        return s.roundToInt()
    }


    fun calculateRightPath(circleList: List<Circle>): List<Point> {
        val xPoints = ArrayList<Int>()
        val yPoints = ArrayList<Int>()
        circleList.forEach { element ->
            xPoints.add(element.x)
            yPoints.add(element.y)
        }
        radiusOfPoint = circleList[0].radius
        spaceBetweenPoints = 20

        for (i in 0 until xPoints.size - 1) {
            val xStart = xPoints[i]
            val xEnd = xPoints[i + 1]
            val yStart = yPoints[i]
            val yEnd = yPoints[i + 1]
            val spaceBetweenTwoPoints = sqrt(
                (xEnd - xStart).toDouble().pow(2.0) + (yEnd - yStart).toDouble().pow(2.0)
            )

            if (spaceBetweenTwoPoints >= (2 * radiusOfPoint) + (spaceBetweenPoints + 5)) {
                rightFullPath.add(Point(xStart, yStart))
            } else {
                rightFullPath.add(Point(xStart, yStart))
                var distanceRatio = .1
                while (distanceRatio < 1) {
                    val xOfNextPoint = (1 - distanceRatio) * xStart + distanceRatio * xEnd
                    val yOfNextPoint = (1 - distanceRatio) * yStart + distanceRatio * yEnd
                    rightFullPath.add(Point(xOfNextPoint.toInt(), yOfNextPoint.toInt()))
                    distanceRatio += .1
                }
            }
        }
        rightFullPath.add(Point(xPoints[xPoints.size - 1], yPoints[yPoints.size - 1]))
        return rightFullPath
    }


    fun checkStrokeInRightPath(x: Float, y: Float, listener: TaskCompletionListener) {
        if (rightFullPath.isNotEmpty()) {
            val xCenter = rightFullPath[0].x
            val yCenter = rightFullPath[0].y
            Log.i(ContentValues.TAG, "point   : x  = $xCenter y  = $yCenter")
            var currentPosition = sqrt(
                (x - xCenter).toDouble().pow(2.0) + (y - yCenter).toDouble().pow(2.0)
            )
            //     Log.i(TAG, "checkStrokeInRightPath:  point  x " + xCenter + "   y " + yCenter);
            Log.i(ContentValues.TAG, "checkStrokeInRightPath:  distance $currentPosition")
            if (currentPosition < radiusOfPoint) {
                storeRightPath(x, y)
                previousPoint = Point(
                    rightFullPath[0]
                )
                rightFullPath.removeAt(0)
                // Log.e(TAG, "store: new point added ");
            } else if (currentPosition > radiusOfPoint) {
                //   Log.i(TAG, "  previousPoint  x " + previousPoint.x + "   previousPoint y " + previousPoint.y);
                Log.i(ContentValues.TAG, "  currentPoint  x $x   currentPoint y $y")
                currentPosition = sqrt(
                    (x - previousPoint.x).toDouble().pow(2.0) + (y - previousPoint.y).toDouble()
                        .pow(2.0)
                )
                if (currentPosition <= radiusOfPoint) {
                    Log.i(ContentValues.TAG, "right :  distance in right path $currentPosition")
                } else {
                    storeWrongPath(x, y)
                    Log.i(ContentValues.TAG, "right :  distance in wrong path $currentPosition")
                    Log.i(ContentValues.TAG, "wrong :  distance $currentPosition")
                }
            }
            if (rightFullPath.isEmpty()) {
                Log.i("TAG", "accuracyPercentage: right " + rightPath.size)
                Log.i("TAG", "accuracyPercentage: wrong " + wrongPath.size)
            }
        } else {
            Log.i(ContentValues.TAG, "checkStrokeInRightPath: path is empty noooooooooooow")
            isPathFinished = true
            listener.onTaskCompleted(accuracyPercentage().toDouble())
        }
    }


    private fun storeRightPath(x: Float, y: Float) {
        //rangeOfFirstPoint();
        if (rightPath.isNotEmpty()) {
            val lastPoint = rightPath[rightPath.size - 1]
            val xRange = Range(
                lastPoint.x - 5,
                lastPoint.x + 5
            )
            val yRange = Range(
                lastPoint.y - 5,
                lastPoint.y + 5
            )
            if (!xRange.contains(x.toInt()) && !yRange.contains(y.toInt())) {
                Log.i("TAG", "storeRightAndWrongPath: right paaaaaaaaaaaaaaath ")
                rightPath.add(Point(x.toInt(), y.toInt()))
            } else if (xRange.contains(x.toInt()) && yRange.contains(y.toInt())) {
                Log.i(ContentValues.TAG, "storeRightAndWrongPath: still within previous domain")
            }
        } else {
            val xFirstPointRange =
                Range(rightFullPath[0].x - radiusOfPoint, rightFullPath[0].x + radiusOfPoint)
            val yFirstPointRange =
                Range(rightFullPath[0].y - radiusOfPoint, rightFullPath[0].y + radiusOfPoint)
            if (xFirstPointRange.contains(x.toInt()) && yFirstPointRange.contains(y.toInt())) {
                rightPath.add(Point(x.toInt(), y.toInt()))
                Log.i(ContentValues.TAG, "right    : first point is registered ")
            } else Log.i(ContentValues.TAG, "wrong    : first point is not registered ")
        }
    }


    private fun storeWrongPath(x: Float, y: Float) {
        if (wrongPath.isNotEmpty()) {
            val lastPoint = wrongPath[wrongPath.size - 1]
            val xRange = Range(
                lastPoint.x - 10,
                lastPoint.x + 10
            )
            val yRange = Range(
                lastPoint.y - 10,
                lastPoint.y + 10
            )
            if (!xRange.contains(x.toInt()) && !yRange.contains(y.toInt())) {
                wrongPath.add(Point(x.toInt(), y.toInt()))
            } else if (xRange.contains(x.toInt()) && yRange.contains(y.toInt())) {
                Log.i(ContentValues.TAG, "storeRightAndWrongPath: still within previous domain")
            }
        } else {
            wrongPath.add(Point(x.toInt(), y.toInt()))
        }
    }

}