package com.example.book_v2.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.book_v2.data.models.Circle
import com.example.book_v2.data.models.Stroke
import com.example.book_v2.data.oop.WritingScore
import com.example.book_v2.services.interfaces.TaskCompletionListener
import com.example.book_v2.utilities.FillLetters
import com.example.book_v2.utilities.HandWritingAccuracy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class DrawLetterView(context: Context?, attributeSet: AttributeSet?) : View(context, attributeSet) {

    private var circlesPaint: Paint
    private var dashedPaint = Paint()
    private var dashPath: DashPathEffect = DashPathEffect(floatArrayOf(6f, 6f), 10.0.toFloat())
    private var arrowPaint = Paint()
    private var writingPaint = Paint()
    private var circlesFillPaint: Paint = Paint()
    private var mX = 0F
    private var mY = 0F
    private val currentPaths = ArrayList<Stroke>()
    private val deletedPaths = ArrayList<Stroke>()

    var color = Color.TRANSPARENT
    var touchEnabled = true

    var currentStrokeColor = Color.BLACK

    private var radius = 0
    var currentStrokeWidth = 4
    private var currentCircleProgressCount: Int = 0
    private var path = Path()
    private var letterPath = Path()
    private var circleList: List<Circle>? = null
    private var fillLetters: FillLetters = FillLetters()
    private var fillCircle: Boolean = false
    lateinit var completionListener: TaskCompletionListener
    private var crossMarksList: ArrayList<Path> = ArrayList()
    private lateinit var handWritingAccuracy: HandWritingAccuracy

    private var currentWrittenPointScore: WritingScore = WritingScore.NOTHING
    private var score = WritingScore.NOTHING

    // private val bitmapPaint: Paint = Paint()
    private var stillWriting = true
    private val fullPath = Path()
    private val bestFitLine = Path()

    var disableFingerTouch = false

    init {

        dashedPaint.pathEffect = dashPath
        dashedPaint.color = Color.BLACK
        dashedPaint.style = Paint.Style.STROKE
        dashedPaint.strokeWidth = 2f

        writingPaint.isAntiAlias = true
        writingPaint.isDither = true
        writingPaint.strokeCap = Paint.Cap.ROUND
        writingPaint.style = Paint.Style.STROKE
        writingPaint.color = Color.BLUE
        writingPaint.strokeJoin = Paint.Join.ROUND
        writingPaint.strokeWidth = currentStrokeWidth.toFloat()

        circlesFillPaint.isAntiAlias = true
        circlesFillPaint.isDither = true
        circlesFillPaint.strokeCap = Paint.Cap.ROUND
        circlesFillPaint.style = Paint.Style.FILL
        circlesFillPaint.color = Color.MAGENTA
        circlesFillPaint.strokeJoin = Paint.Join.ROUND

        arrowPaint.isAntiAlias = true
        arrowPaint.isDither = true
        arrowPaint.strokeCap = Paint.Cap.ROUND
        arrowPaint.style = Paint.Style.STROKE
        arrowPaint.strokeJoin = Paint.Join.ROUND
        arrowPaint.strokeWidth = 5f
        arrowPaint.color = Color.parseColor("#FF0000")

        circlesPaint = Paint()
        circlesPaint.isAntiAlias = true
        circlesPaint.isDither = true
        circlesPaint.strokeCap = Paint.Cap.ROUND
        circlesPaint.style = Paint.Style.STROKE
        circlesPaint.color = Color.BLACK
        circlesPaint.strokeJoin = Paint.Join.ROUND
        circlesPaint.strokeWidth = 4F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.drawColor(color)
        for (i in 0..3) {
            canvas.drawLine(
                0f,
                (i * height / 4).toFloat(),
                width.toFloat(),
                (i * height / 4).toFloat(),
                dashedPaint,
            )
        }

        canvas.drawPath(fullPath, circlesPaint)

        // canvas.drawPath(bestFitLine, writingPaint)

        crossMarksList.forEach { element ->
            canvas.drawPath(element, arrowPaint)
        }

        canvas.drawPath(optimalPath, optimalPathPaint)
        canvas.drawPath(halfOptimalPath, halfOptimalPathPaint)
        canvas.drawPath(quarterOptimalPath, quarterOptimalPathPaint)
        canvas.drawPath(outOfRangePath, outOfRangePathPaint)

//        currentPaths.forEach { stroke ->
//            writingPaint.color = stroke.color
//            writingPaint.strokeWidth = stroke.width.toFloat()
//            canvas.drawPath(stroke.path, writingPaint)
//        }

        canvas.drawPath(letterPath, circlesPaint)

        circleList?.let {
            if (stillWriting) {
                for (circle in circleList!!) {
                    if (circle.circleCount < currentCircleProgressCount) {
                        canvas.drawCircle(
                            circleList!![circle.circleCount].x.toFloat(),
                            circleList!![circle.circleCount].y.toFloat(),
                            circleList!![circle.circleCount].radius.toFloat(),
                            circlesFillPaint,
                        )
                    }
                }
            }

            if (fillCircle) {
                canvas.drawCircle(
                    circleList!![currentCircleProgressCount].x.toFloat(),
                    circleList!![currentCircleProgressCount].y.toFloat(),
                    circleList!![currentCircleProgressCount].radius.toFloat(),
                    circlesFillPaint,
                )
                currentCircleProgressCount++
                fillCircle = false
            }
            // draw guide arrow
            if (currentCircleProgressCount < circleList!!.size) {
                //   Log.e(TAG, "onDraw: $currentCircleProgressCount")
                val guidingArrow = drawArrow(
                    circleList!![currentCircleProgressCount].x + (radius + (width / 15)),
                    circleList!![currentCircleProgressCount].y,
                )
                canvas.drawPath(guidingArrow, arrowPaint)
            } else {
                letterPath.reset()
                stillWriting = false
            }
        }

        canvas.restore()
    }

    private fun onTouchStart(x: Float, y: Float) {
        path = Path()
        currentPaths.add(Stroke(currentStrokeColor, currentStrokeWidth, path))
        path.reset()
        path.moveTo(x, y)

        mX = x
        mY = y
    }

    private fun onTouchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy: Float = abs(y - mY)
        if (dx >= ConstrainedMatchingView.TOUCH_TOLERANCE || dy >= ConstrainedMatchingView.TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun onTouchUp() {
        path.lineTo(mX, mY)
        path = Path()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        if (disableFingerTouch) {
            if (event.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS) {
                touchEnabled = false
            }
        }else{
            touchEnabled = true
        }

        if (touchEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onTouchStart(x, y)
                    colorWritingBasedOnScore(x, y, score)
                    fillCircle =
                        fillLetters.fillCircleOnTouch(x, y, circleList, currentCircleProgressCount)
                    score = handWritingAccuracy.observeUserHandWriting(x.toInt(), y.toInt())
                    invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    onTouchMove(x, y)
                    colorWritingBasedOnScore(x, y, score)
                    fillCircle =
                        fillLetters.fillCircleOnTouch(x, y, circleList, currentCircleProgressCount)
                    score = handWritingAccuracy.observeUserHandWriting(x.toInt(), y.toInt())
                    invalidate()
                }

                MotionEvent.ACTION_UP -> {
                    onTouchUp()
                    colorWritingBasedOnScore(x, y, score)
                    fillCircle =
                        fillLetters.fillCircleOnTouch(x, y, circleList, currentCircleProgressCount)
                    score = handWritingAccuracy.observeUserHandWriting(x.toInt(), y.toInt())
                    invalidate()
                }
            }
            currentWrittenPointScore = score
            return true
        }
        return false
    }

    private fun drawArrow(touchX: Int, touchY: Int): Path {
        val arrowPath = Path()

        if ((currentCircleProgressCount + 1) < circleList!!.size) {
            arrowPath.moveTo((touchX - (radius + 5)).toFloat(), touchY.toFloat())
            arrowPath.lineTo((touchX - (radius + width / 20)).toFloat(), touchY.toFloat())
            arrowPath.lineTo(
                (touchX - (radius + width / 35)).toFloat(),
                (touchY - height / 35).toFloat(),
            )
            arrowPath.moveTo((touchX - (radius + width / 20)).toFloat(), touchY.toFloat())
            arrowPath.lineTo(
                (touchX - (radius + width / 35)).toFloat(),
                (touchY + height / 35).toFloat(),
            )
        }
        return arrowPath
    }

    private var firstPointOfLetterX = 0F
    private var firstPointOfLetterY = 0F
    fun setCircleList(circleList: List<Circle>) {
        this.circleList = circleList
        radius = circleList[0].radius
        handWritingAccuracy = HandWritingAccuracy(completionListener)
        handWritingAccuracy.radius = radius
        handWritingAccuracy.space = circleList[0].distance
        handWritingAccuracy.calculateRightPath(circleList)

        for (i in circleList) {
            letterPath.addCircle(i.x.toFloat(), i.y.toFloat(), radius.toFloat(), Path.Direction.CW)
            invalidate()
        }
        bestFitLine.moveTo(
            handWritingAccuracy.fullPath[0].x.toFloat(),
            handWritingAccuracy.fullPath[0].y.toFloat(),
        )
        handWritingAccuracy.fullPath.let {
            it.forEachIndexed { index, e ->
                if (index == 0) {
                    bestFitLine.lineTo(e.x.toFloat(), e.y.toFloat())
                } else {
                    val distanceBetweenPoints = sqrt(
                        (it[index].x - it[index - 1].x).toDouble()
                            .pow(2.0) + (it[index].y - it[index - 1].y).toDouble()
                            .pow(2.0),
                    )

                    if (distanceBetweenPoints > handWritingAccuracy.space) {
                        bestFitLine.moveTo(e.x.toFloat(), e.y.toFloat())
                    } else {
                        bestFitLine.lineTo(e.x.toFloat(), e.y.toFloat())
                    }
                }

                //  fullPath.addCircle(e.x.toFloat(), e.y.toFloat(), 3F, Path.Direction.CW)
            }
        }

        firstPointOfLetterX = handWritingAccuracy.fullPath[0].x.toFloat()
        firstPointOfLetterY = handWritingAccuracy.fullPath[0].y.toFloat()
    }

    companion object {
        const val TOUCH_TOLERANCE = 4
        const val TAG = "draw letter view"
    }

    fun undoLastAction() {
        if (currentPaths.isNotEmpty()) {
            val undoneAction = currentPaths.removeAt(currentPaths.lastIndex)
            deletedPaths.add(undoneAction)
        }

        invalidate()
    }

    fun redoLastAction() {
        if (deletedPaths.isNotEmpty()) {
            val redoneAction = deletedPaths.removeAt(deletedPaths.lastIndex)
            currentPaths.add(redoneAction)
        }
        invalidate()
    }

    private fun drawCrossMark(centerX: Float, centerY: Float) {
        val size = width / 20
        val cross = Path()
        val left = centerX - size / 2
        val right = centerX + size / 2
        val top = centerY - size / 2
        val bottom = centerY + size / 2

        cross.moveTo(left, top)
        cross.lineTo(right, bottom)
        cross.moveTo(left, bottom)
        cross.lineTo(right, top)
        crossMarksList.add(cross)
        invalidate()
    }

    fun handleInaccurateHandWriting(x: Float, y: Float) {
        disableTouch(3000)
        drawCrossMark(x, y)
        MainScope().launch(Dispatchers.Main) {
            for (i in 0..1) {
                color = Color.RED
                delay(500)
                invalidate()
                color = Color.TRANSPARENT
                delay(500)
                invalidate()
            }
        }
    }

    private fun disableTouch(duration: Long) {
        touchEnabled = false
        Handler().postDelayed(
            { touchEnabled = true },
            duration,
        )
    }

    private val optimalPath = Path()
    private val halfOptimalPath = Path()
    private val quarterOptimalPath = Path()
    private val outOfRangePath = Path()
    private val optimalPathPaint = Paint()
    private val halfOptimalPathPaint = Paint()
    private val quarterOptimalPathPaint = Paint()
    private val outOfRangePathPaint = Paint()
    private var currentPath = 0

    init {
        optimalPathPaint.color = Color.GREEN
        halfOptimalPathPaint.color = Color.YELLOW
        quarterOptimalPathPaint.color = Color.rgb(255, 204, 91)
        outOfRangePathPaint.color = Color.RED
        optimalPathPaint.strokeWidth = 1F
        halfOptimalPathPaint.strokeWidth = 1F
        quarterOptimalPathPaint.strokeWidth = 1F
        outOfRangePathPaint.strokeWidth = 1F
    }

    private var lastOptimalPoint = PointF(firstPointOfLetterX, firstPointOfLetterY)
    private var lastHalfOptimalPoint = PointF(firstPointOfLetterX, firstPointOfLetterY)
    private var lastQuarterOptimalPoint = PointF(firstPointOfLetterX, firstPointOfLetterY)
    private var lastOutOfRangePoint = PointF(firstPointOfLetterX, firstPointOfLetterY)

    private fun colorWritingBasedOnScore(x: Float, y: Float, score: WritingScore) {
        when (score) {
            WritingScore.Optimal -> {
                Color.GREEN
                // connectPaths(lastOptimalPoint, PointF(x, y), optimalPath)
                optimalPath.addCircle(x, y, 5F, Path.Direction.CCW)
                // lastOptimalPoint = PointF(x, y)
                currentPath = 1
            }

            WritingScore.HalfOptimal -> {
                Color.YELLOW
                // connectPaths(lastHalfOptimalPoint, PointF(x, y), optimalPath)
                halfOptimalPath.addCircle(x, y, 5F, Path.Direction.CCW)
                // lastHalfOptimalPoint = PointF(x, y)
                currentPath = 2
            }

            WritingScore.QuarterOptimal -> {
                Color.rgb(255, 204, 91)
                // connectPaths(lastQuarterOptimalPoint, PointF(x, y), optimalPath)
                quarterOptimalPath.addCircle(x, y, 5F, Path.Direction.CCW)
                // lastQuarterOptimalPoint = PointF(x, y)
                currentPath = 3
            }

            WritingScore.OutOfRange -> {
                Color.RED
                // connectPaths(lastOutOfRangePoint, PointF(x, y), optimalPath)
                outOfRangePath.addCircle(x, y, 5F, Path.Direction.CCW)
                // lastOutOfRangePoint = PointF(x, y)
                currentPath = 4
            }

            WritingScore.NOTHING -> {
            }

            WritingScore.Continue -> {
                when (currentPath) {
                    1 -> {
                        // connectPaths(lastOptimalPoint, PointF(x, y), optimalPath)
                        optimalPath.addCircle(x, y, 3F, Path.Direction.CCW)
                        // lastOptimalPoint = PointF(x, y)
                    }

                    2 -> {
                        // connectPaths(lastHalfOptimalPoint, PointF(x, y), optimalPath)
                        halfOptimalPath.addCircle(x, y, 3F, Path.Direction.CCW)
                        // lastHalfOptimalPoint = PointF(x, y)
                    }

                    3 -> {
                        // connectPaths(lastQuarterOptimalPoint, PointF(x, y), optimalPath)
                        quarterOptimalPath.addCircle(x, y, 3F, Path.Direction.CCW)
                        // lastQuarterOptimalPoint = PointF(x, y)
                    }

                    4 -> {
                        // connectPaths(lastOutOfRangePoint, PointF(x, y), optimalPath)
                        outOfRangePath.addCircle(x, y, 3F, Path.Direction.CCW)
                        // lastOutOfRangePoint = PointF(x, y)
                    }
                }
            }
        }
    }

    private fun connectPaths(lastPoint: PointF, newPoint: PointF, path: Path) {
        val distanceBetweenPoints = sqrt(
            (newPoint.x - lastPoint.x).toDouble().pow(2.0) + (newPoint.y - lastPoint.y).toDouble()
                .pow(2.0),
        )
        if (distanceBetweenPoints < 3) {
            path.quadTo(
                lastPoint.x,
                lastPoint.y,
                (newPoint.x + lastPoint.x) / 2,
                (newPoint.y + lastPoint.y) / 2,
            )
        } else {
            path.moveTo(newPoint.x, newPoint.y)
        }
    }
}
