package com.example.book_v2.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.book_v2.data.models.Stroke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class LetterPredictionView(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {


    val TAG = "CustomViewWriteLetter"
    private var writingPaint = Paint()
    private var whitePaint = Paint()
    private val bitmapPaint = Paint(Paint.DITHER_FLAG)
    private var bitmap: Bitmap? = null
    private var color = Color.BLACK
    private var flashColor = Color.TRANSPARENT
    private var currentPaths = ArrayList<Stroke>()
    private var mX = 0f
    private var mY: Float = 0f
    private lateinit var path: Path
    var currentStrokeColor = color
    var currentStrokeWidth = 10
    private lateinit var canvas: Canvas

    init {

        writingPaint.isAntiAlias = true
        writingPaint.isDither = true
        writingPaint.strokeCap = Paint.Cap.ROUND
        writingPaint.style = Paint.Style.STROKE

        whitePaint.isAntiAlias = true
        whitePaint.isDither = true
        whitePaint.strokeCap = Paint.Cap.ROUND
        whitePaint.style = Paint.Style.STROKE
        whitePaint.strokeWidth = 24f


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        canvas.drawColor(Color.BLACK)
    }


    private fun onTouchStart(x: Float, y: Float) {
        path = Path()
        path.reset()
        currentPaths.add(Stroke(Color.WHITE, 24, path))
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
        canvas.drawPath(path, writingPaint)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.drawColor(flashColor)

        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, bitmapPaint) }

        for (stroke in currentPaths) {
            writingPaint.color = Color.WHITE
            writingPaint.strokeWidth = stroke.width.toFloat()
            canvas.drawPath(stroke.path, writingPaint)
        }
        canvas.restore()
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

    fun wrongLetter() {
        MainScope().launch(Dispatchers.Main) {
            for (i in 0..2) {
                flashColor = Color.RED
                delay(500)
                invalidate()
                flashColor = Color.TRANSPARENT
                delay(500)
                invalidate()
            }
            currentPaths.clear()
        }
        invalidate()
    }

    fun rightLetter() {
        MainScope().launch(Dispatchers.Main) {
            for (i in 0..2) {
                flashColor = Color.GREEN
                delay(500)
                invalidate()
                flashColor = Color.TRANSPARENT
                delay(500)
                invalidate()
            }
        }
        invalidate()
    }


    fun getLetterImage(): Bitmap {
        return bitmap!!
    }
}