package com.example.book_v2.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.book_v2.data.models.Stroke
import com.example.book_v2.ui.customviews.DrawLetterView.Companion.TOUCH_TOLERANCE
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs

class ColoringImageView(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {

    private var paint: Paint = Paint()
    private var bitmap: Bitmap? = null
    private var mX = 0f
    private var mY: Float = 0f
    private lateinit var path: Path
    private val currentPaths: MutableList<Stroke> = ArrayList()
    private val deletedPaths: MutableList<Stroke> = ArrayList()
    var currentStrokeColor = Color.BLUE
    var currentStrokeWidth = 0
    private lateinit var canvas: Canvas

    // var bitmap = BitmapFactory.decodeFile()

    init {
        paint.isAntiAlias = true
        paint.isDither = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        paint.color = Color.GREEN
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 5F
//        writingPaint = Paint()
//        writingPaint.isAntiAlias = true
//        writingPaint.isDither = true
//        writingPaint.strokeCap = Paint.Cap.ROUND
//        writingPaint.style = Paint.Style.STROKE
//        writingPaint.color = Color.GREEN
//        writingPaint.strokeJoin = Paint.Join.ROUND
//        writingPaint.strokeWidth = 5F
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
        canvas.drawPath(path, paint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        // canvas.drawColor(Color.BLUE)
        bitmap?.let {
            canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        }

        canvas.drawColor(0x00AAAAAA)

        for (stroke in currentPaths) {
            paint.color = stroke.color
            paint.strokeWidth = stroke.width.toFloat()
            canvas.drawPath(stroke.path, paint)
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

    fun saveBitmap(path: String): Bitmap {
        bitmap!!.compress(
            Bitmap.CompressFormat.PNG,
            100,
            FileOutputStream(File(path)),
        )
        Log.d("TAG", "saveBitmap: $")
        return bitmap!!
    }

    fun setBitmapInCanvas(bitmap: Bitmap) {
        this.bitmap = bitmap
        canvas = Canvas(bitmap)
    }

    fun undoLastAction() {
        if (currentPaths.size > 0) {
            deletedPaths.add(currentPaths.removeLast())
        }
        invalidate()
    }

    fun redoLastAction() {
        if (deletedPaths.size > 0) {
            currentPaths.add(deletedPaths.removeLast())
        }
        invalidate()
    }
}
