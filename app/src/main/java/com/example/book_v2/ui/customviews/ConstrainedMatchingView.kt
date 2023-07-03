package com.example.book_v2.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.book_v2.R
import com.example.book_v2.data.models.Circle
import com.example.book_v2.data.models.Stroke
import com.example.book_v2.utilities.FillLetters
import kotlin.math.abs

class ConstrainedMatchingView(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {
    private var circlesPaint: Paint
    private var borderPaint: Paint
    private var lineEndShapesPaint: Paint = Paint()
    private var writingPaint: Paint = Paint()
    private var bitmapPaint: Paint
    private var circlesFillPaint: Paint = Paint()
    private var clipboard: Bitmap? = null
    private val mPaths: MutableList<Stroke> = ArrayList()
    private var currentStrokeColor = Color.BLACK
    private var radius = 5
    private var currentStrokeWidth = 4
    private var currentCircleProgressCount: Int = 0
    private var mX = 0f
    private var mY: Float = 0f
    private lateinit var path: Path
    private var canvasBorderPath: Path = Path()
    private var circleList: List<Circle>? = null
    private var fillLetters: FillLetters = FillLetters()
    private var fillCircle: Boolean = false
    private val arrowPaint = Paint()


    init {

        arrowPaint.isAntiAlias = true
        arrowPaint.isDither = true
        arrowPaint.strokeCap = Paint.Cap.ROUND
        arrowPaint.style = Paint.Style.STROKE
        arrowPaint.strokeJoin = Paint.Join.ROUND
        arrowPaint.strokeWidth = 5f
        arrowPaint.color = Color.parseColor("#FF0000")

        writingPaint.isAntiAlias = true
        writingPaint.isDither = true
        writingPaint.strokeCap = Paint.Cap.ROUND
        writingPaint.style = Paint.Style.STROKE
        writingPaint.color = Color.BLACK
        writingPaint.strokeJoin = Paint.Join.ROUND
        writingPaint.strokeWidth = 5F

        circlesFillPaint.isAntiAlias = true
        circlesFillPaint.isDither = true
        circlesFillPaint.strokeCap = Paint.Cap.ROUND
        circlesFillPaint.style = Paint.Style.FILL
        circlesFillPaint.color = Color.YELLOW
        circlesFillPaint.strokeJoin = Paint.Join.ROUND

        lineEndShapesPaint.isAntiAlias = true
        lineEndShapesPaint.isDither = true
        lineEndShapesPaint.strokeCap = Paint.Cap.ROUND
        lineEndShapesPaint.style = Paint.Style.FILL
        lineEndShapesPaint.strokeJoin = Paint.Join.ROUND
        lineEndShapesPaint.strokeWidth = 20f
        lineEndShapesPaint.color = Color.parseColor("#FF0000")

        borderPaint = Paint()
        borderPaint.style = Paint.Style.FILL
        borderPaint.color = Color.BLUE
        borderPaint.strokeWidth = 1F


        bitmapPaint = Paint()
        bitmapPaint.color = 0x00AAAAAA

        circlesPaint = Paint()
        circlesPaint.isAntiAlias = true
        circlesPaint.isDither = true
        circlesPaint.strokeCap = Paint.Cap.ROUND
        circlesPaint.style = Paint.Style.STROKE
        circlesPaint.color = Color.BLACK
        circlesPaint.strokeJoin = Paint.Join.ROUND
        circlesPaint.strokeWidth = 5F

        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 10F

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        clipboard = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        // background color of the canvas.
        canvas.drawColor(resources.getColor(R.color.item_dark_bg))

        clipboard?.let {
            canvas.drawBitmap(it, 0F, 0F, bitmapPaint)
        }



        for (stroke in mPaths) {
            writingPaint.color = stroke.color
            writingPaint.strokeWidth = stroke.width.toFloat()
            canvas.drawPath(stroke.path, writingPaint)
        }
        circleList?.let {
            for (circle in circleList!!) {
                canvas.drawCircle(
                    circle.x.toFloat(),
                    circle.y.toFloat(),
                    circle.radius.toFloat(),
                    circlesPaint
                )
            }
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

            if (currentCircleProgressCount < circleList!!.size) {
                //   Log.e(TAG, "onDraw: $currentCircleProgressCount")
                val guidingArrow = drawArrow(
                    circleList!![currentCircleProgressCount].x + (radius + (width / 15)),
                    circleList!![currentCircleProgressCount].y
                )
                canvas.drawPath(guidingArrow, arrowPaint)
            }
        }
//
//        canvas.drawRoundRect(0f, 0f, width.toFloat()
//            , height.toFloat()
//            , 30f, 30f
//            , borderPaint);
        // draw border around canvas
        //canvas.drawPath(drawBorder(), borderPaint)
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
                fillCircle =
                    fillLetters.fillCircleOnTouch(x, y, circleList, currentCircleProgressCount)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                onTouchMove(x, y)
                fillCircle =
                    fillLetters.fillCircleOnTouch(x, y, circleList, currentCircleProgressCount)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                onTouchUp()
                fillCircle =
                    fillLetters.fillCircleOnTouch(x, y, circleList, currentCircleProgressCount)
                invalidate()
            }
        }
        return true
    }

    private fun drawBorder(): Path {
        canvasBorderPath.moveTo(0f, 0f)
        canvasBorderPath.lineTo(width.toFloat(), 0f)
        canvasBorderPath.lineTo(width.toFloat(), height.toFloat())
        canvasBorderPath.lineTo(0f, height.toFloat())
        canvasBorderPath.lineTo(0f, 0f)
        canvasBorderPath.close()
        return canvasBorderPath
    }

    fun setCircleList(circleList: List<Circle>) {
        this.circleList = circleList
        radius = circleList[0].radius
    }

    fun setCurrentStrokeColor(currentStrokeColor: Int) {
        this.currentStrokeColor = currentStrokeColor
    }

    fun setCurrentStrokeWidth(currentStrokeWidth: Int) {
        this.currentStrokeWidth = currentStrokeWidth
    }

    companion object {
        const val TOUCH_TOLERANCE = 4
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
}