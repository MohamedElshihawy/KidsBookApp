package com.example.book_v2.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.book_v2.ui.customviews.ConstrainedMatchingView.Companion.TOUCH_TOLERANCE
import com.example.book_v2.data.models.Stroke
import kotlin.math.abs

class WriteTextView(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {


    val TAG = "CustomViewWriteLetter"
    private var letterPaint = Paint()
    private var writingPaint = Paint()
    private val bitmapPaint = Paint(Paint.DITHER_FLAG)
    private var text: String? = null
    private var bitmap: Bitmap? = null
    private var color = Color.BLACK
    private var mX = 0f
    private var mY: Float = 0f
    private lateinit var path: Path
    private val currentPaths: MutableList<Stroke> = ArrayList()
    private val deletedPaths: MutableList<Stroke> = ArrayList(5)
    var currentStrokeColor = color
    var currentStrokeWidth = 10

    init {

        letterPaint.isAntiAlias = true
        letterPaint.isDither = true
        letterPaint.strokeCap = Paint.Cap.ROUND
        letterPaint.color = color
        letterPaint.style = Paint.Style.STROKE

        letterPaint.textSize = 500f
        letterPaint.strokeWidth = 2f

        writingPaint.isAntiAlias = true
        writingPaint.isDither = true
        writingPaint.strokeCap = Paint.Cap.ROUND
        writingPaint.style = Paint.Style.STROKE

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        letterPaint.textSize = (3 * w / 4).toFloat()
        Log.e(TAG, "onSizeChanged: $w")
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
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
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun onTouchUp() {
        path.lineTo(mX, mY)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.drawColor(0x00AAAAAA)
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, bitmapPaint) }

        text?.let {
            canvas.drawText(
                it, (width / 4).toFloat(), (height - height / 5).toFloat(),
                letterPaint
            )
        }

        for (stroke in currentPaths) {
            writingPaint.color = stroke.color
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


    fun undoLastAction() {
        if (currentPaths.size > 0)
            deletedPaths.add(currentPaths.removeLast())
        invalidate()
    }

    fun redoLastAction() {
        if (deletedPaths.size > 0)
            currentPaths.add(deletedPaths.removeLast())
        invalidate()
    }

}