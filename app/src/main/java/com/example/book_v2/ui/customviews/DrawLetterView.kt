package com.example.book_v2.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
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

    private var mCanvas: Canvas? = null
    private var clipboard: Bitmap? = null

    // private val bitmapPaint: Paint = Paint()
    private var stillWriting = true
    private val fullPath = Path()


    init {

        dashedPaint.pathEffect = dashPath
        dashedPaint.color = Color.BLACK
        dashedPaint.style = Paint.Style.STROKE
        dashedPaint.strokeWidth = 2f

        writingPaint.isAntiAlias = true
        writingPaint.isDither = true
        writingPaint.strokeCap = Paint.Cap.ROUND
        writingPaint.style = Paint.Style.STROKE
        writingPaint.color = Color.BLACK
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        clipboard = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(clipboard!!)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.drawColor(color)
        for (i in 0..3) {
            mCanvas!!.drawLine(
                0f,
                (i * height / 4).toFloat(),
                width.toFloat(),
                (i * height / 4).toFloat(),
                dashedPaint
            )
        }

        canvas.drawPath(fullPath,circlesPaint)

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
                            circlesFillPaint
                        )
                    }
                }
            }

            if (fillCircle) {
                canvas.drawCircle(
                    circleList!![currentCircleProgressCount].x.toFloat(),
                    circleList!![currentCircleProgressCount].y.toFloat(),
                    circleList!![currentCircleProgressCount].radius.toFloat(),
                    circlesFillPaint
                )
                currentCircleProgressCount++
                fillCircle = false
            }
            // draw guide arrow
            if (currentCircleProgressCount < circleList!!.size) {
             //   Log.e(TAG, "onDraw: $currentCircleProgressCount")
                val guidingArrow = drawArrow(
                    circleList!![currentCircleProgressCount].x + (radius + (width / 15)),
                    circleList!![currentCircleProgressCount].y
                )
                canvas.drawPath(guidingArrow, arrowPaint)
            } else {
                letterPath.reset()
                stillWriting = false
            }
        }

        //canvas.drawBitmap(clipboard!!, 0F, 0F, bitmapPaint)

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

        crossMarksList.forEach { element ->
            mCanvas!!.drawPath(element, arrowPaint)
        }
    }

    private fun onTouchUp() {
        path.lineTo(mX, mY)
        path = Path()
        mCanvas!!.drawPath(optimalPath, optimalPathPaint)
        mCanvas!!.drawPath(halfOptimalPath, halfOptimalPathPaint)
        mCanvas!!.drawPath(quarterOptimalPath, quarterOptimalPathPaint)
        mCanvas!!.drawPath(outOfRangePath, outOfRangePathPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        if (touchEnabled) {
//
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
                (touchY - height / 35).toFloat()
            )
            arrowPath.moveTo((touchX - (radius + width / 20)).toFloat(), touchY.toFloat())
            arrowPath.lineTo(
                (touchX - (radius + width / 35)).toFloat(),
                (touchY + height / 35).toFloat()
            )
        }
        return arrowPath
    }

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
        mCanvas!!.drawPath(letterPath, circlesPaint)

        val bestFitLine = Path()
        bestFitLine.moveTo(
            handWritingAccuracy.fullPath[0].x.toFloat(),
            handWritingAccuracy.fullPath[0].y.toFloat()
        )
        handWritingAccuracy.fullPath.let {
            it.forEach { e ->
                bestFitLine.lineTo(e.x.toFloat(), e.y.toFloat())
              //  fullPath.addCircle(e.x.toFloat(), e.y.toFloat(), 3F, Path.Direction.CW)
            }
        }
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
            color = Color.RED
            delay(500)
            invalidate()
            color = Color.TRANSPARENT
            delay(500)
            invalidate()
        }
//
//            currentPaths.clear()
//            deletedPaths.clear()
//            handWritingAccuracy?.resetAccuracyValue()
//            currentCircleProgressCount = 0
//        }
//
//        invalidate()
    }

    //
    private fun disableTouch(duration: Long) {
        touchEnabled = false
        Handler().postDelayed(
            { touchEnabled = true }, duration
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


    private fun colorWritingBasedOnScore(x: Float, y: Float, score: WritingScore) {
        when (score) {
            WritingScore.Optimal -> {
                Color.GREEN
                optimalPath.addCircle(x, y, 5F, Path.Direction.CCW)
                currentPath = 1
            }

            WritingScore.HalfOptimal -> {
                Color.YELLOW
                halfOptimalPath.addCircle(x, y, 5F, Path.Direction.CCW)
                currentPath = 2
            }

            WritingScore.QuarterOptimal -> {
                Color.rgb(255, 204, 91)
                quarterOptimalPath.addCircle(x, y, 5F, Path.Direction.CCW)
                currentPath = 3
            }

            WritingScore.OutOfRange -> {
                Color.RED
                outOfRangePath.addCircle(x, y, 5F, Path.Direction.CCW)
                currentPath = 4
            }

            WritingScore.NOTHING -> {
            }

            WritingScore.Continue -> {
                when (currentPath) {
                    1 -> {
                        optimalPath.addCircle(x, y, 3F, Path.Direction.CCW)
                    }

                    2 -> {
                        halfOptimalPath.addCircle(x, y, 3F, Path.Direction.CCW)
                    }

                    3 -> {
                        quarterOptimalPath.addCircle(x, y, 3F, Path.Direction.CCW)
                    }

                    4 -> {
                        outOfRangePath.addCircle(x, y, 3F, Path.Direction.CCW)
                    }
                }
            }
        }
    }
}