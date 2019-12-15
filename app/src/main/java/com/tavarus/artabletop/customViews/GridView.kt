package com.tavarus.artabletop.customViews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.tavarus.artabletop.R
import com.tavarus.artabletop.dataModels.MaterialEnum
import com.tavarus.artabletop.dataModels.NewBoard
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class GridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var stonePaint = Paint()
    var nonePaint = Paint()
    var whitePaint = Paint()
    var guidePaint = Paint()

    var board: NewBoard = NewBoard()
    set(value) {
        field = value
        calculateDimensions()
    }

    var size = 0.0f

    var mOffsetX = 0.0f
    var mOffsetY = 0.0f

    var scaling = false

    var flingAnimatorX: ValueAnimator? = null
    var flingAnimatorY: ValueAnimator? = null

    private val mGestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (!scaling) {
                flingAnimatorX?.cancel()
                flingAnimatorY?.cancel()
                mOffsetX = checkBound(mOffsetX - distanceX)
                mOffsetY = checkBound(mOffsetY - distanceY)
                invalidate()
            }
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            flingAnimatorX?.cancel()
            flingAnimatorY?.cancel()

            val scaledVX = velocityX /2000
            val scaledVY = velocityY / 2000
            val time = (max(abs(velocityX), abs(velocityY))/5).toLong()
            val distanceX = scaledVX * time
            val distanceY = scaledVY * time

            flingAnimatorX = ValueAnimator.ofFloat(mOffsetX, mOffsetX + distanceX)
            flingAnimatorX?.duration = time
            flingAnimatorX?.interpolator = DecelerateInterpolator()
            flingAnimatorX?.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                mOffsetX = checkBound(animatedValue)
                if (mOffsetX != animatedValue) {
                    it.cancel()
                }
                invalidate()
            }

            flingAnimatorY = ValueAnimator.ofFloat(mOffsetY, mOffsetY + distanceY)
            flingAnimatorY?.duration = time
            flingAnimatorY?.interpolator = DecelerateInterpolator()
            flingAnimatorY?.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                mOffsetY = checkBound(animatedValue)
                if (mOffsetY != animatedValue) {
                    it.cancel()
                }
                invalidate()
            }

            flingAnimatorY?.start()
            flingAnimatorX?.start()

            return true
        }
    }

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            scaling = true
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            scaling = false
            return super.onScaleEnd(detector)
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {

            val futureSize = size*detector.scaleFactor

            size = max(min(futureSize, min(width, height).toFloat()), min(width/10, height/10).toFloat())
            invalidate()

            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    private val mGestureDetector = GestureDetector(context, mGestureListener)

    private val mOnTouchListener = OnTouchListener { view, ev ->
        mScaleDetector.onTouchEvent(ev)
        mGestureDetector.onTouchEvent(ev)
    }


    init {
        setWillNotDraw(false)
        stonePaint.strokeWidth = 4.0f
        stonePaint.color = ContextCompat.getColor(context, R.color.colorStone)
        nonePaint.strokeWidth = 4.0f
        nonePaint.color = ContextCompat.getColor(context, R.color.colorNone)
        whitePaint.strokeWidth = 4.0f
        whitePaint.color = Color.WHITE
        guidePaint.strokeWidth = 2.0f
        guidePaint.color = ContextCompat.getColor(context, R.color.colorGuideLine)
        setOnTouchListener(mOnTouchListener)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDimensions()
    }

    fun checkBound(offset: Float): Float {
        if (abs(offset) > 10 * size) {
            return if (offset < 0) (-10 * size) else 10 * size
            //TODO: Bounce back
        }
        return offset
    }

    fun calculateDimensions() {
        if (board.width() < 1 || board.height() < 1) {
            return
        }

        val cellWidth = (width.toFloat() - 120) / 4
        val cellHeight = (height.toFloat() - 120) / 4
        size = if (cellWidth < cellHeight) cellWidth else cellHeight

        if (board.width() % 2 == 1) {
            mOffsetX -= size/2
        }
        if (board.height() % 2 == 1) {
            mOffsetY -= size/2
        }

        invalidate()
    }

    fun selectPaint(material: MaterialEnum): Paint {
        return when(material) {
            MaterialEnum.STONE -> stonePaint
            MaterialEnum.NONE -> nonePaint
        }
    }

    fun drawVerticalRhomb(left: Float, leftOffset: Float, right: Float, rightOffset: Float, top: Float, bottom: Float) : Path {
        val path = Path()
        path.moveTo(left, top + leftOffset)
        path.lineTo(right, top + rightOffset)
        path.lineTo(right, bottom - rightOffset)
        path.lineTo(left, bottom - leftOffset)
        path.lineTo(left, top + leftOffset)
        path.close()
        return path
    }

    fun drawHorizontalRhomb(top: Float, topOffset: Float, bottom: Float, bottomOffset: Float, left: Float, right: Float) : Path {
        val path = Path()
        path.moveTo(left + topOffset, top)
        path.lineTo(right - topOffset, top)
        path.lineTo(right - bottomOffset, bottom)
        path.lineTo(left + bottomOffset, bottom)
        path.lineTo(left + topOffset, top)
        path.close()
        return path
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val gridSpace = size / 4

        var heightRemainder = (height % size) / 2
        if (height > width) {
            heightRemainder += size / 2
        }
        for (i in -4..(height / gridSpace).toInt() + 4) {
            val horizontal = (i * gridSpace) + heightRemainder + mOffsetY % (size)
            // adding 0.1 and checking < 1 for imprecisions
            val thickLine = (abs(horizontal - mOffsetY - height/2) + 0.1) % size < 1
            val paint = if (thickLine) whitePaint else guidePaint
            canvas.drawLine(0.0f, horizontal, width.toFloat(), horizontal, paint)

        }

        val widthRemainder = (width % size) / 2
        for (i in -4..(width / gridSpace).toInt() + 4) {
            val vertical = (i * gridSpace) + widthRemainder + mOffsetX % (size)
            // adding 0.1 and checking < 1 for imprecisions
            val thickLine = (abs(vertical - mOffsetX - width/2) + 0.1) % size < 1
            val paint = if (thickLine) whitePaint else guidePaint

            canvas.drawLine(vertical, 0.0f, vertical, height.toFloat(), paint)
        }

        val rectOffset = gridSpace + 5
        val rhombOffset = gridSpace - 10

        if (board.width() < 1 || board.height() < 1) {
            return
        }

        board.tiles.forEachIndexed { hi, list ->
            val heightIndex = hi - board.tiles.size/2
            list.forEachIndexed { wi, tile ->
                val widthIndex = wi - list.size/2
                val top = heightIndex * size + 10 + mOffsetY + height/2
                val left = widthIndex * size + 10 + mOffsetX + width/2
                val right = (widthIndex + 1) * size - 10 + mOffsetX + width/2
                val bottom = (heightIndex + 1) * size - 10 + mOffsetY + height/2
                val leftRhomb = drawVerticalRhomb(left, 0.0f, left + rhombOffset, rhombOffset, top + 10, bottom - 10)
                val rightRhomb = drawVerticalRhomb(right - rhombOffset, rhombOffset, right, 0.0f, top + 10, bottom - 10)
                val topRhomb = drawHorizontalRhomb(top, 0.0f, top + rhombOffset, rhombOffset, left + 10, right - 10)
                val bottomRhomb = drawHorizontalRhomb(bottom - rhombOffset, rhombOffset, bottom, 0.0f, left + 10, right - 10)

                canvas.drawPath(leftRhomb, selectPaint(tile.leftWall))
                canvas.drawPath(rightRhomb, selectPaint(tile.rightWall))
                canvas.drawPath(topRhomb, selectPaint(tile.topWall))
                canvas.drawPath(bottomRhomb, selectPaint(tile.bottomWall))

                canvas.drawRect(left + rectOffset, top + rectOffset, right - rectOffset, bottom - rectOffset, selectPaint(tile.material))

            }
        }

    }
}
