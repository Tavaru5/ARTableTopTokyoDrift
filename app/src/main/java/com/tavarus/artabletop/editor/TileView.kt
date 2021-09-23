package com.tavarus.artabletop.editor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tavarus.artabletop.R
import com.tavarus.artabletop.dataModels.MaterialEnum
import com.tavarus.artabletop.dataModels.Tile
import com.tavarus.artabletop.extensions.hide
import com.tavarus.artabletop.extensions.show
import kotlinx.android.synthetic.main.tile_view.view.*

class TileView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var shown = false

    init {
        inflate(context, R.layout.tile_view, this)
    }

    fun selectWallPaint(material: MaterialEnum) = when(material) {
        MaterialEnum.STONE -> ContextCompat.getColor(context, R.color.colorStoneWall)
        MaterialEnum.WOOD -> ContextCompat.getColor(context, R.color.colorWoodWall)
        MaterialEnum.NONE -> Color.TRANSPARENT
    }

    fun bindTile(tile: Tile, onClick: (TileClickLocation) -> Unit ) {
        bindTile(tile)
        setOnClickListener { onClick(TileClickLocation.CENTER) }
        leftWall.setOnClickListener { onClick(TileClickLocation.LEFT) }
        rightWall.setOnClickListener { onClick(TileClickLocation.RIGHT) }
        topWall.setOnClickListener { onClick(TileClickLocation.TOP) }
        bottomWall.setOnClickListener { onClick(TileClickLocation.BOTTOM) }
        show()
        shown = true
    }

    fun bindTile(tile: Tile) {
        setBackgroundColor(ContextCompat.getColor(context, tile.material.color))
        leftWall.setBackgroundColor(selectWallPaint(tile.leftWall))
        rightWall.setBackgroundColor(selectWallPaint(tile.rightWall))
        topWall.setBackgroundColor(selectWallPaint(tile.topWall))
        bottomWall.setBackgroundColor(selectWallPaint(tile.bottomWall))
    }

    fun unBindTile() {
        leftWall.setBackgroundColor(Color.TRANSPARENT)
        rightWall.setBackgroundColor(Color.TRANSPARENT)
        topWall.setBackgroundColor(Color.TRANSPARENT)
        bottomWall.setBackgroundColor(Color.TRANSPARENT)
        hide()
        shown = false
    }

    enum class TileClickLocation {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        CENTER,
    }

}
