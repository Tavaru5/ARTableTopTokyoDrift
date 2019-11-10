package com.tavarus.artabletop.customViews

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.tavarus.artabletop.models.Board
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.content.ContextCompat
import com.tavarus.artabletop.R
import com.tavarus.artabletop.models.MaterialEnum
import com.tavarus.artabletop.models.NewBoard
import java.lang.Float.min
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




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

    var heightMargin = 60.0f
    var widthMargin = 60.0f
    var size = 0.0f

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
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDimensions()
    }

    fun calculateDimensions() {
        if (board.width() < 1 || board.height() < 1) {
            return
        }

        val cellWidth = (width.toFloat() - 120) / board.width()
        val cellHeight = (height.toFloat() - 120) / board.height()
        if (cellWidth < cellHeight) {
            size = cellWidth
            heightMargin = (height.toFloat() - width.toFloat()) / 2 + 60
        } else {
            size = cellHeight
            widthMargin = (width.toFloat() - height.toFloat()) / 2 + 60
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

        if (board.width() < 1 || board.height() < 1) {
            return
        }

        val offset = size/4

        var heightRemainder = height.rem(size)/2
        if (height > width) {
            heightRemainder += size/2
        }
        for (i in -1..(height/size).toInt()) {
            val horizontal = (i * size) + heightRemainder
            canvas.drawLine(0.0f, horizontal, width.toFloat(), horizontal, whitePaint)
            canvas.drawLine(0.0f, horizontal + offset, width.toFloat(), horizontal + offset, guidePaint)
            canvas.drawLine(0.0f, horizontal + 2 * offset, width.toFloat(), horizontal + 2 * offset, guidePaint)
            canvas.drawLine(0.0f, horizontal + 3 * offset, width.toFloat(), horizontal + 3 * offset, guidePaint)

        }

        val widthRemainder = width.rem(size)/2
        for (i in -1..(width/size).toInt()) {
            val vertical = (i * size) + widthRemainder
            canvas.drawLine(vertical, 0.0f, vertical, height.toFloat(), whitePaint)
            canvas.drawLine(vertical + offset, 0.0f, vertical + offset, height.toFloat(), guidePaint)
            canvas.drawLine(vertical + 2 * offset, 0.0f, vertical + 2 * offset, height.toFloat(), guidePaint)
            canvas.drawLine(vertical + 3 * offset, 0.0f, vertical + 3 * offset, height.toFloat(), guidePaint)
        }

        val rectOffset = offset + 5
        val rhombOffset = offset - 10
        board.tiles.forEachIndexed { heightIndex, list ->
            list.forEachIndexed { widthIndex, tile ->
                val top = heightIndex * size + heightMargin + 10
                val left = widthIndex * size + widthMargin + 10
                val right = (widthIndex + 1) * size + widthMargin - 10
                val bottom = (heightIndex + 1) * size + heightMargin - 10
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
