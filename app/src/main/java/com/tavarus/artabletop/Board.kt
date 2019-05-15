package com.tavarus.artabletop

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.Node


class Board (private val width: Int,private val height: Int, private val size: Float, context: Context) {
    private var tileRenderable: ModelRenderable? = null

    init {
        MaterialFactory.makeOpaqueWithColor(context, com.google.ar.sceneform.rendering.Color(Color.BLACK))
            .thenAccept { material ->
                tileRenderable = ShapeFactory.makeCube(Vector3(0.96f * size,0.01f,0.96f * size), Vector3(0f,0.1f,0f),material)
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
            tile.localPosition = Vector3(x, 0.1f, y)
        }
        return base
    }
}