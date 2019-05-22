package com.tavarus.artabletop

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.Node


class Board (
    private val width: Int,
    private val height: Int,
    private val size: Float,
    private val xWalls: Array<Int>,
    private val yWalls: Array<Int>,
    context: Context
) {
    private var tileRenderable: ModelRenderable? = null
    private var xWall: ModelRenderable? = null
    private var yWall: ModelRenderable? = null

    private val tileSeparation = 0.04f


    init {
        MaterialFactory.makeOpaqueWithColor(context, com.google.ar.sceneform.rendering.Color(Color.BLACK))
            .thenAccept { material ->
                tileRenderable = ShapeFactory.makeCube(
                    Vector3(
                        (1f - tileSeparation / 2) * size,
                        0.01f,
                        (1f - tileSeparation / 2) * size),
                    Vector3(0f,0f,0f),
                    material)
                xWall = ShapeFactory.makeCube(Vector3(size, size , tileSeparation * size), Vector3(0f,0f,0f),material)
                yWall = ShapeFactory.makeCube(Vector3(tileSeparation * size, size , size), Vector3(0f,0f,0f),material)

            }
    }

    fun boardNode(): Node {
        val base = Node()
        base.localPosition = Vector3(0f, 0.2f, 0f)
        for (index in 0 until height * width) {
            val tile = Node()
            val x = 0f - ((width * size - size) / 2) + (index.rem(width) * size)
            val y = 0f - ((height * size - size) / 2) + (index.div(height) * size)
            tile.setParent(base)
            tile.renderable = tileRenderable
            tile.localPosition = Vector3(x, 0f, y)
        }

        for (wallIndex in xWalls) {
            if (wallIndex < (height + 1) * width) {
                val wall = Node()
                val x = 0f - ((width * size - size) / 2) + (wallIndex.rem(width) * size)
                val y = 0f - ((height * size) / 2) + (wallIndex.div(height) * size)
                wall.setParent(base)
                wall.renderable = xWall
                wall.localPosition = Vector3(x, size/2, y)
            }
        }

        for (wallIndex in yWalls) {
            if (wallIndex < height * (width + 1)) {
                val wall = Node()
                val x = 0f - ((width * size) / 2) + (wallIndex.div(width) * size)
                val y = 0f - ((height * size - size) / 2) + (wallIndex.rem(height) * size)
                wall.setParent(base)
                wall.renderable = yWall
                wall.localPosition = Vector3(x, size/2, y)
            }
        }
        return base
    }

}