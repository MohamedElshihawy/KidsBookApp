package com.example.book_v2.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.book_v2.R
import com.example.book_v2.data.models.CurvedLine
import com.example.book_v2.data.models.Stroke
import com.example.book_v2.ui.customviews.ConstrainedMatchingView.Companion.TOUCH_TOLERANCE
import kotlin.math.abs

class FreeMatchingView(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {

    private val smallMatchingView = "Small Matching View"
    private val mediumMatchingView = "Medium Matching View"
    private val largeMatchingView = "Large Matching View"
    private val dashedStraightLine = "Dashed Curved Line"

    private var dashPath: DashPathEffect = DashPathEffect(floatArrayOf(6f, 6f), 4.0.toFloat())
    private var circlesPaint: Paint = Paint()
    private var paint: Paint = Paint()
    private var textPaint: Paint = Paint()
    private var writingPaint: Paint = Paint()
    private var mX = 0f
    private var mY: Float = 0f
    private lateinit var path: Path
    private val mPaths: MutableList<Stroke> = ArrayList()
    private var currentStrokeColor = Color.BLACK
    private var currentStrokeWidth = 10
    var viewSize: String? = null
    var lineShape: String? = null
    private lateinit var curvedLine: CurvedLine

    var letter1 = "أ"
    var letter2 = "ب"

    private var x1: Int = 0
    private var y1: Int = 0
    private var y2: Int = 0
    private var x2: Int = 0
    private var r: Int = 0

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.color = Color.BLACK
        paint.pathEffect = dashPath


        writingPaint.isAntiAlias = true
        writingPaint.style = Paint.Style.STROKE
        writingPaint.strokeWidth = 10f
        writingPaint.color = Color.BLACK


        val customTypeface = context?.let {
            ResourcesCompat.getFont(it, R.font.blabello)
        }

        textPaint.typeface = customTypeface
        textPaint.isAntiAlias = true
        textPaint.isDither = true
        textPaint.color = Color.BLACK
        textPaint.strokeCap = Paint.Cap.ROUND
        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.strokeJoin = Paint.Join.ROUND
        textPaint.textSize = 80f

        circlesPaint.isAntiAlias = true
        circlesPaint.style = Paint.Style.FILL
        circlesPaint.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.save()

        canvas.drawColor(resources.getColor(R.color.primary_color))

        for (stroke in mPaths) {
            writingPaint.color = stroke.color
            writingPaint.strokeWidth = stroke.width.toFloat()
            canvas.drawPath(stroke.path, writingPaint)

        }

        viewSize?.let {
            curvedLine = if (lineShape.equals(dashedStraightLine, true)) {
                drawnCurvedLine(it)
            } else {
                drawStraightLine(it)
            }
            canvas.drawPath(curvedLine.linePath!!, paint)
            canvas.drawCircle(
                curvedLine.x1!!.toFloat(),
                curvedLine.y1!!.toFloat(),
                curvedLine.radius!!.toFloat(),
                circlesPaint
            )
            canvas.drawCircle(
                curvedLine.x2!!.toFloat(),
                curvedLine.y2!!.toFloat(),
                curvedLine.radius!!.toFloat(),
                circlesPaint
            )
            val quarterR = curvedLine.radius!! / 4
            textPaint.textSize = (quarterR * 4).toFloat()

//            canvas.save()
//            canvas.rotate(
//                180f, (curvedLine.x1!! - quarterR).toFloat(),
//                (curvedLine.y1!! + quarterR).toFloat()
//            )
//
            canvas.drawText(
                letter2,
                (curvedLine.x1!! - quarterR).toFloat(),
                (curvedLine.y1!! + quarterR).toFloat(),
                textPaint
            )
            canvas.drawText(
                letter1, (curvedLine.x2!! - quarterR).toFloat(),
                (curvedLine.y2!! + quarterR).toFloat(), textPaint
            )
        }
        canvas.restore()
    }


    private fun onTouchStart(x: Float, y: Float) {
        path = Path()
        mPaths.add(Stroke(currentStrokeColor, currentStrokeWidth, path))
        path.reset()
        path.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun onTouchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy: Float = abs(y - mY)

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun onTouchUp() {
        path.lineTo(mX, mY)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onTouchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                onTouchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                onTouchUp()
                invalidate()
            }
        }
        return true
    }

    private fun drawStraightLine(viewSize: String): CurvedLine {
        var curvedLine = CurvedLine()

        when (viewSize) {
            smallMatchingView -> {
                curvedLine = smallStraightLine()
            }
            mediumMatchingView -> {
                curvedLine = mediumStraightLine()
            }
            largeMatchingView -> {
                curvedLine = largeStraightLine()
            }
        }
        return curvedLine
    }

    private fun drawnCurvedLine(
        lineShape: String,
    ): CurvedLine {
        var curvedLine = CurvedLine()
        when (lineShape) {
            smallMatchingView -> {
                curvedLine =
                    smallSizeView()
            }
            mediumMatchingView -> {
                curvedLine =
                    mediumSizeView()
            }
            largeMatchingView -> {
                curvedLine =
                    largeSizeView()
            }
        }
        return curvedLine
    }

    private fun smallSizeView(

    ): CurvedLine {
        //suppose that the each circle will take 40 percentage of height
        // and that we leave 10 percentage margin at top and bottom
        val curvedLine = CurvedLine()
        val linePath = Path()
        if (width >= height) {
            r = (.2 * width).toInt()
            x1 = (.25 * width).toInt()
            y1 = (.95 * height).toInt() - r
            y2 = y1
            x2 = (width - x1)
            linePath.moveTo((x1 + r).toFloat(), (y1).toFloat())
            linePath.quadTo(
                (width / 2).toFloat(),
                0.toFloat(),
                (x2 - r).toFloat(),
                (y2).toFloat()
            )
        } else {
            r = (.2 * height).toInt()
            x1 = (.95 * width).toInt() - r
            y1 = (.25 * height).toInt()
            y2 = height - y1
            x2 = x1
            linePath.moveTo((x1 - r).toFloat(), (y1).toFloat())
            linePath.quadTo(
                0.toFloat(),
                (height / 2).toFloat(),
                (x2 - r).toFloat(),
                y2.toFloat()
            )
        }
        curvedLine.x1 = x1
        curvedLine.y1 = y1
        curvedLine.x2 = x2
        curvedLine.y2 = y2
        curvedLine.radius = r
        curvedLine.linePath = linePath
        return curvedLine
    }

    private fun mediumSizeView(
    ): CurvedLine {
        //suppose that the each circle will take 40 percentage of height
        // and that we leave 10 percentage margin at top and bottom
        val linePath = Path()
        val curvedLine = CurvedLine()
        if (width >= height) {
            r = (.1 * width).toInt()
            x1 = (.15 * width).toInt()
            y1 = (.95 * height).toInt() - r
            x2 = .85.toInt()
            y2 = y1
            linePath.moveTo((x1 + r).toFloat(), (y1).toFloat())
            linePath.quadTo(
                (width / 2).toFloat(),
                0.toFloat(),
                (x2 - r).toFloat(),
                (y2).toFloat()
            )
        } else {
            r = (.1 * height).toInt()
            x1 = (.95 * width).toInt() - r
            y1 = (.15 * height).toInt()
            x2 = x1
            y2 = (.85 * height).toInt()

            linePath.moveTo((x1 - r).toFloat(), (y1).toFloat())
            linePath.quadTo(
                0.toFloat(),
                (height / 2).toFloat(),
                (x2 - r).toFloat(),
                y2.toFloat()
            )
        }
        curvedLine.x1 = x1
        curvedLine.y1 = y1
        curvedLine.x2 = x2
        curvedLine.y2 = y2
        curvedLine.radius = r
        curvedLine.linePath = linePath
        return curvedLine
    }

    private fun largeSizeView(
    ): CurvedLine {
        //suppose that the each circle will take 40 percentage of height
        // and that we leave 10 percentage margin at top and bottom
        val linePath = Path()
        val curvedLine = CurvedLine()
        if (width >= height) {
            r = (.05 * width).toInt()
            x1 = (.075 * width).toInt()
            y1 = (.95 * height).toInt() - r
            x2 = (.925 * width).toInt()
            y2 = y1

            linePath.moveTo((x1 + r).toFloat(), (y1).toFloat())
            linePath.quadTo(
                (width / 2).toFloat(),
                0.toFloat(),
                (x2 - r).toFloat(),
                (y2).toFloat()
            )
        } else {
            r = (.05 * height).toInt()
            x1 = (.95 * width).toInt() - r
            y1 = (.075 * height).toInt()
            x2 = x1
            y2 = (.925 * width).toInt()
            linePath.moveTo((x1 - r).toFloat(), (y1).toFloat())
            linePath.quadTo(
                0.toFloat(),
                (height / 2).toFloat(),
                (x2 - r).toFloat(),
                y2.toFloat()
            )
        }
        curvedLine.x1 = x1
        curvedLine.y1 = y1
        curvedLine.x2 = x2
        curvedLine.y2 = y2
        curvedLine.radius = r
        curvedLine.linePath = linePath
        return curvedLine
    }


    private fun smallStraightLine(): CurvedLine {
        val curvedLine = CurvedLine()
        val linePath = Path()
        if (width >= height) {
            x1 = (.125 * width).toInt()
            y1 = (.1 * height).toInt()
            x2 = (.875 * width).toInt()
            y2 = y1
            r = (.1 * width).toInt()
            linePath.moveTo((x1 + r).toFloat(), y1.toFloat())
            linePath.lineTo((x2 - r).toFloat(), y2.toFloat())
        } else {
            x1 = (.5 * width).toInt()
            y1 = (.125 * height).toInt()
            x2 = x1
            y2 = (.875 * height).toInt()
            r = (.1 * height).toInt()
            linePath.moveTo(x1.toFloat(), (y1 + r).toFloat())
            linePath.lineTo(x2.toFloat(), (y2 - r).toFloat())
        }
        curvedLine.x1 = x1
        curvedLine.y1 = y1
        curvedLine.x2 = x2
        curvedLine.y2 = y2
        curvedLine.radius = r
        curvedLine.linePath = linePath
        return curvedLine
    }

    private fun mediumStraightLine(): CurvedLine {
        val curvedLine = CurvedLine()
        val linePath = Path()
        if (width >= height) {
            x1 = (.15 * width).toInt()
            y1 = (.5 * height).toInt()
            x2 = (.850 * width).toInt()
            y2 = y1
            r = (.125 * width).toInt()
            linePath.moveTo((x1 + r).toFloat(), y1.toFloat())
            linePath.lineTo((x2 - r).toFloat(), y2.toFloat())
        } else {
            x1 = (.5 * width).toInt()
            y1 = (.15 * height).toInt()
            x2 = x1
            y2 = (.850 * height).toInt()
            r = (.125 * height).toInt()
            linePath.moveTo(x1.toFloat(), (y1 + r).toFloat())
            linePath.lineTo(x2.toFloat(), (y2 - r).toFloat())
        }
        curvedLine.x1 = x1
        curvedLine.y1 = y1
        curvedLine.x2 = x2
        curvedLine.y2 = y2
        curvedLine.radius = r
        curvedLine.linePath = linePath
        return curvedLine
    }


    private fun largeStraightLine(): CurvedLine {
        val curvedLine = CurvedLine()
        val linePath = Path()
        if (width >= height) {
            x1 = (.125 * width).toInt()
            y1 = (.5 * height).toInt()
            x2 = (.875 * width).toInt()
            y2 = y1
            r = (.0725 * width).toInt()
            linePath.moveTo((x1 + r).toFloat(), y1.toFloat())
            linePath.lineTo((x2 - r).toFloat(), y2.toFloat())
        } else {
            x1 = (.5 * width).toInt()
            y1 = (.125 * height).toInt()
            x2 = x1
            y2 = (.875 * height).toInt()
            r = (.0725 * height).toInt()
            linePath.moveTo(x1.toFloat(), (y1 + r).toFloat())
            linePath.lineTo(x2.toFloat(), (y2 - r).toFloat())
        }
        curvedLine.x1 = x1
        curvedLine.y1 = y1
        curvedLine.x2 = x2
        curvedLine.y2 = y2
        curvedLine.radius = r
        curvedLine.linePath = linePath

        return curvedLine
    }


}