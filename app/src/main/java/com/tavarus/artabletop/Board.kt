package com.tavarus.artabletop

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.Node


class Board (private val width: Int, private val height: Int, private val size: Float, context: Context) {
    private var tileRenderable: ModelRenderable? = null
    private var xWall: ModelRenderable? = null
    private var yWall: ModelRenderable? = null


    init {
        MaterialFactory.makeOpaqueWithColor(context, com.google.ar.sceneform.rendering.Color(Color.BLACK))
            .thenAccept { material ->
                tileRenderable = ShapeFactory.makeCube(Vector3(0.96f * size,0.01f,0.96f * size), Vector3(0f,0f,0f),material)
                xWall = ShapeFactory.makeCube(Vector3(0.96f * size,size ,0.08f * size), Vector3(0f,0f,0f),material)
                yWall = ShapeFactory.makeCube(Vector3(0.08f * size,size ,0.96f * size), Vector3(0f,0f,0f),material)

            }
    }

    fun boardNode(): Node {
        val base = Node()
        for (index in 0 until height * width) {
            val tile = Node()
            val x = 0f - ((width * size - size) / 2) + (index.rem(width) * size)
            val y = 0f - ((height * size - size) / 2) + (index.div(height) * size)
            tile.setParent(base)
            tile.renderable = tileRenderable
            tile.localPosition = Vector3(x, 0f, y)
        }
        addWalls(base, arrayOf(0, 3, 8), arrayOf(0, 3, 8))
        return base
    }

    fun addWalls(base: Node, xWalls: Array<Int>, yWalls: Array<Int>) {
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
    }
}