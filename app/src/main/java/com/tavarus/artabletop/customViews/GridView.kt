package com.tavarus.artabletop.customViews

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.tavarus.artabletop.models.Board
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT


class GridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var blackPaint = Paint()

    var board: Board = Board()
    set(value) {
        field = value
        calculateDimensions()
    }

    var cellHeight = 0.0f
    var cellWidth = 0.0f

    init {
        setWillNotDraw(false)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDimensions()
    }

    fun calculateDimensions() {
        Log.d("KOG", "calculate dimenstion")
        Log.d("KOG", board.height.toString())
        if (board.width < 1 || board.height < 1) {
            return
        }

        cellWidth = width.toFloat() / board.width
        cellHeight = height.toFloat() / board.height

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)

        blackPaint.strokeWidth = 4.0f

        if (board.width < 1 || board.height < 1) {
            return
        }

        for (i in 0..board.width+1) {
            canvas.drawLine(i * cellWidth, 0.0f, i * cellWidth, height.toFloat(), blackPaint)
        }

        for (i in 0..board.height+1) {
            canvas.drawLine(0.0f, i * cellHeight, width.toFloat(), i * cellHeight, blackPaint)
        }
    }
}
