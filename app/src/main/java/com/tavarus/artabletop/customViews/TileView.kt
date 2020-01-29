package com.tavarus.artabletop.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tavarus.artabletop.R
import com.tavarus.artabletop.dataModels.MaterialEnum
import com.tavarus.artabletop.dataModels.Tile

class TileView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    lateinit var tile: Tile
    var left = 0.0f
    var right = 0.0f
    var top = 0.0f
    var bottom = 0.0f
    var stonePaint = Paint()
    var nonePaint = Paint()

    init {
        setWillNotDraw(false)
        stonePaint.strokeWidth = 14.0f
        stonePaint.color = ContextCompat.getColor(context, R.color.colorStone)
        nonePaint.strokeWidth = 4.0f
        nonePaint.color = ContextCompat.getColor(context, R.color.colorNone)
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

    override fun onDraw(canvas: Canvas?) {
        val gridSpace = width / 4.0f
        val rhombOffset = gridSpace - 10

        val topRhomb = drawHorizontalRhomb(10.0f, 0.0f, rhombOffset, rhombOffset - 10, 15.0f, width-15.0f)
        val botRhomb = drawHorizontalRhomb(height - rhombOffset,rhombOffset - 10, height - 10.0f, 0.0f, 15.0f, width-15.0f)
        val leftRhomb = drawVerticalRhomb(10.0f, 0.0f, rhombOffset, rhombOffset - 10, 15.0f, height-15.0f)
        val rightRhomb = drawVerticalRhomb(width - rhombOffset, rhombOffset - 10, width - 10.0f, 0.0f, 15.0f, height-15.0f)

        canvas?.drawRect(gridSpace, gridSpace, width - gridSpace, height - gridSpace, selectPaint(tile.material))
        canvas?.drawPath(topRhomb, selectPaint(tile.topWall))
        canvas?.drawPath(botRhomb, selectPaint(tile.bottomWall))
        canvas?.drawPath(leftRhomb, selectPaint(tile.leftWall))
        canvas?.drawPath(rightRhomb, selectPaint(tile.rightWall))
    }

}
