package com.tavarus.artabletop.editor

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View.OnTouchListener
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import com.tavarus.artabletop.dataModels.MaterialEnum
import com.tavarus.artabletop.dataModels.Board
import kotlin.math.*


class GridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    val TILE_MARGIN = 20
    val MAX_SIZE = 20

    var board: Board = Board()
        set(value) {
            field = value
            calculateDimensions()
        }

    var size = 0.0f

    // Offset of the user's scroll from the center
    var xOffset = 0.0f
    var yOffset = 0.0f

    var topMostTileIndex = MAX_SIZE / 2
    var bottomMostTileIndex = MAX_SIZE / 2
    var leftMostTileIndex = MAX_SIZE / 2
    var rightMostTileIndex = MAX_SIZE / 2

    var scaling = false

    var flingAnimatorX: ValueAnimator? = null
    var flingAnimatorY: ValueAnimator? = null

    var numOfViews = 0

    /*  This is a 2D list mirroring that of the tiles in the actual Board. Each value will be
     *  an index pointing to the new TileView list, or -1 if this index is not currently shown in a
     *  TileView
     *  Accessed as tileIndices[heightindex][widthindex]
     */
    val tileIndices: MutableList<MutableList<Int>> = MutableList(MAX_SIZE) {
        MutableList(MAX_SIZE) {
            -1
        }
    }

    /*  This is the list of our TileViews. This list is blind; the only thing that reads from it is
     *  the android view code.
     */
    val tileViews: MutableList<TileView> = mutableListOf()

    /*  This list mirrors the above tile views list. For each tile view it shows if that view is
     *  currently bound to a tile or if it is free to be recycled. This list could probably be
     *  removed and combined with the tileViews list.
     */
    val tileViewsInUse: MutableList<Boolean> = mutableListOf()


    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return false
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
                xOffset = catchHorizontalBound(xOffset - distanceX)
                yOffset = catchVerticalBound(yOffset - distanceY)
                redraw()
            }
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return false
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {


            flingAnimatorX?.cancel()
            flingAnimatorY?.cancel()

            val scaledVX = velocityX / 2000
            val scaledVY = velocityY / 2000
            val time = (max(abs(velocityX), abs(velocityY)) / 5).toLong()
            val distanceX = scaledVX * time
            val distanceY = scaledVY * time

            flingAnimatorX = ValueAnimator.ofFloat(xOffset, xOffset + distanceX)
            flingAnimatorX?.duration = time
            flingAnimatorX?.interpolator = DecelerateInterpolator()
            flingAnimatorX?.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                xOffset = catchHorizontalBound(animatedValue)
                redraw()
                if (xOffset != animatedValue) {
                    it.cancel()
                }
            }

            flingAnimatorY = ValueAnimator.ofFloat(yOffset, yOffset + distanceY)
            flingAnimatorY?.duration = time
            flingAnimatorY?.interpolator = DecelerateInterpolator()
            flingAnimatorY?.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                yOffset = catchVerticalBound(animatedValue)
                redraw()
                if (yOffset != animatedValue) {
                    it.cancel()
                }
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
        val futureSize = size * detector.scaleFactor
        size = max(
            min(futureSize, min(width, height).toFloat()),
            min(width / 10, height / 10).toFloat()
        )

        //In here, if we scale to a corner, we need to move the offset
        xOffset = catchHorizontalBound(xOffset)
        yOffset = catchVerticalBound(yOffset)

        calculateViews()
        redraw()
        return true
    }
}

private val scaleDetector = ScaleGestureDetector(context, scaleListener)

private val gestureDetector = GestureDetector(context, gestureListener)

private val onTouchListener = OnTouchListener { view, ev ->
    scaleDetector.onTouchEvent(ev)
    gestureDetector.onTouchEvent(ev)
}

override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return onTouchListener.onTouch(this, ev)
}

init {
    setWillNotDraw(false)
    setOnTouchListener(onTouchListener)
}

private fun redraw() {
    updateTileViews()
    invalidate()
}

override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    calculateDimensions()
}

fun catchVerticalBound(offset: Float): Float {
    val verticalBound = (10f * size) - height / 2
    if (abs(offset) > verticalBound) {
        // Adding 1 in the negative case to cut the bound just before the edge
        return if (offset < 0) (1 - verticalBound) else verticalBound
    }
    return offset
}

fun catchHorizontalBound(offset: Float): Float {
    val horizontalBound = (10f * size) - width / 2
    if (abs(offset) > horizontalBound) {
        return if (offset < 0) (1 - horizontalBound) else horizontalBound
    }
    return offset
}

fun updateTileViews() {
    val leftMostVisibleTile = calculateVisibleTile(xOffset + width / 2)
    val rightMostVisibleTile = calculateVisibleTile(xOffset - width / 2)
    val topMostVisibleTile = calculateVisibleTile(yOffset + height / 2)
    val bottomMostVisibleTile = calculateVisibleTile(yOffset - height / 2)

    if (
        leftMostVisibleTile != leftMostTileIndex
        || rightMostVisibleTile != rightMostTileIndex
        || topMostVisibleTile != topMostTileIndex
        || bottomMostVisibleTile != bottomMostTileIndex
    ) {
        leftMostTileIndex = leftMostVisibleTile
        rightMostTileIndex = rightMostVisibleTile
        topMostTileIndex = topMostVisibleTile
        bottomMostTileIndex = bottomMostVisibleTile

        logState()

        var freeTileIndex = 0

        //Find all tiles that are no longer showing that have a tile index still
        for (hi in 0 until topMostTileIndex union ((bottomMostTileIndex + 1) until 20)) {
            tileIndices[hi].forEachIndexed { gridIndex, tileIndex ->
                if (tileIndex != -1) {
                    tileViews[tileIndex].unBindTile()
                    tileViewsInUse[tileIndex] = false
                    tileIndices[hi][gridIndex] = -1
                }
            }
        }
        for (hi in topMostTileIndex..bottomMostTileIndex) {
            for (wi in 0 until leftMostTileIndex union ((rightMostTileIndex + 1) until 20)) {
                if (tileIndices[hi][wi] != -1) {
                    tileViews[tileIndices[hi][wi]].unBindTile()
                    tileViewsInUse[tileIndices[hi][wi]] = false
                    tileIndices[hi][wi] = -1
                }
            }
            //Find tiles in the new area that don't have a view yet
            for (wi in leftMostTileIndex..rightMostTileIndex) {
                if (tileIndices[hi][wi] == -1) {
                    while (freeTileIndex < tileViewsInUse.size && tileViewsInUse[freeTileIndex]) {
                        freeTileIndex += 1
                    }
                    if (freeTileIndex < tileViewsInUse.size) {
                        tileViewsInUse[freeTileIndex] = true
                        tileViews[freeTileIndex].bindTile(board.tiles[0][hi][wi]) { tile: TileView.TileClickLocation ->
                            onTileClick(hi, wi, tile)
                        }

                        tileIndices[hi][wi] = freeTileIndex

                    } else {
                        val newTileView = TileView(context)
                        newTileView.bindTile(board.tiles[0][hi][wi]) { tile: TileView.TileClickLocation ->
                            onTileClick(hi, wi, tile)
                        }
                        addView(newTileView)
                        tileViews.add(newTileView)
                        tileViewsInUse.add(true)
                        tileIndices[hi][wi] = freeTileIndex
                    }
                }

            }
        }
    }

    tileIndices.forEachIndexed { hi, row ->
        val heightIndex = hi - tileIndices.size / 2
        row.forEachIndexed { wi, tileIndex ->
            if (tileIndex != -1) {
                val widthIndex = wi - row.size / 2
                val centery = heightIndex * size + yOffset + height / 2
                val centerx = widthIndex * size + xOffset + width / 2
                val tileSize = size.toInt() - 2 * TILE_MARGIN
                val params = LayoutParams(tileSize, tileSize)
                params.addRule(ALIGN_PARENT_LEFT, TRUE)
                params.addRule(ALIGN_PARENT_TOP, TRUE)
                params.leftMargin = centerx.toInt() + TILE_MARGIN
                params.rightMargin = (width - centerx + size).toInt() + TILE_MARGIN
                params.bottomMargin = (height - centery + size).toInt() + TILE_MARGIN
                params.topMargin = centery.toInt() + TILE_MARGIN
                tileViews[tileIndex].layoutParams = params
            }
        }
    }
}

fun calculateDimensions() {
    val cellWidth = (width.toFloat() - 120) / 4
    val cellHeight = (height.toFloat() - 120) / 4
    size = if (cellWidth < cellHeight) cellWidth else cellHeight

    if (board.width() % 2 == 1) {
        xOffset -= size / 2
    }
    if (board.height() % 2 == 1) {
        yOffset -= size / 2
    }

    calculateViews()
    redraw()
}

fun calculateViews() {
    val totalTiles = board.height() * board.width()
    val visibleTiles = ((ceil(width / size) + 1) * (ceil(height / size) + 1)).toInt()
    numOfViews = min(totalTiles, visibleTiles)
}

fun calculateVisibleTile(tileVisibilityOffset: Float) =
    floor(MAX_SIZE / 2 - tileVisibilityOffset / size).toInt().coerceIn(0 until MAX_SIZE)

fun onTileClick(
    hi: Int,
    wi: Int,
    tileClickLocation: TileView.TileClickLocation
) {
    when (tileClickLocation) {
        TileView.TileClickLocation.LEFT -> board.tiles[0][hi][wi].leftWall =
            selectClickMaterial(board.tiles[0][hi][wi].leftWall)
        TileView.TileClickLocation.RIGHT -> board.tiles[0][hi][wi].rightWall =
            selectClickMaterial(board.tiles[0][hi][wi].rightWall)
        TileView.TileClickLocation.TOP -> board.tiles[0][hi][wi].topWall =
            selectClickMaterial(board.tiles[0][hi][wi].topWall)
        TileView.TileClickLocation.BOTTOM -> board.tiles[0][hi][wi].bottomWall =
            selectClickMaterial(board.tiles[0][hi][wi].bottomWall)
        TileView.TileClickLocation.CENTER -> board.tiles[0][hi][wi].material =
            selectClickMaterial(board.tiles[0][hi][wi].material)
    }
    tileViews[tileIndices[hi][wi]].bindTile(board.tiles[0][hi][wi])
}

// This will be expanded on for more materials later
fun selectClickMaterial(currentMaterial: MaterialEnum) =
    when (currentMaterial) {
        MaterialEnum.STONE -> MaterialEnum.NONE
        MaterialEnum.WOOD -> MaterialEnum.NONE
        MaterialEnum.NONE -> MaterialEnum.STONE
    }


fun logState() {
//        Log.d("KOG", "START")
//        for (list in tileIndices) {
//            Log.d("KOG", list.toString())
//        }
//        Log.d("KOG", tileViewsInUse.toString())
//        Log.d("KOG", "tile views in use in use: " + tileViewsInUse.filter { it }.size)
//        Log.d("KOG", "tile views that think they're showing: " + tileViews.filter { it.isShown }.size)
//        Log.d("KOG", "tileviews allocated: " + tileViews.size)
//        Log.d("KOG", "Height: " + height)
//        Log.d("KOG", "Width: " + width)
//        Log.d("KOG", "offsetY: " + yOffset)
//        Log.d("KOG", "offsetX: " + xOffset)
//        Log.d("KOG", "size: " + size)
//        Log.d("KOG", "topMostTileIndex: " + topMostTileIndex)
//        Log.d("KOG", "botMostTileIndex: " + bottomMostTileIndex)
//        Log.d("KOG", "leftMostTileIndex: " + leftMostTileIndex)
//        Log.d("KOG", "rightMostTileIndex: " + rightMostTileIndex)
//        Log.d("KOG", "STOP")
}
}
