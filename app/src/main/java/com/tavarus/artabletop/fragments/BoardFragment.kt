package com.tavarus.artabletop.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.tavarus.artabletop.App
import com.tavarus.artabletop.models.Board
import com.tavarus.artabletop.viewModels.BoardViewModel
import javax.inject.Inject

class BoardFragment : ArFragment() {


    var canPlace: Boolean = false

    @Inject
    lateinit var boardViewModel: BoardViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.applicationContext as App).provideBoardComponent().inject(this)
        (activity?.applicationContext as App).provideCoreComponent().inject(this)

        val boardObserver = Observer<Board> { newBoard ->
            canPlace = (newBoard != null)
            //Hide/show loading
        }

        boardViewModel.board.observe(this, boardObserver)


        setOnTapArPlaneListener { hitResult: HitResult, _: Plane, _: MotionEvent ->
            if (canPlace) {
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arSceneView.scene)
                anchorNode.addChild(getBoardNode(boardViewModel.board.value!!))
            }
        }
    }

    fun getBoardNode(board: Board): Node {

        val height = board.height
        val width = board.width

        val tileSeparation = 0.04f
        val size = 0.05f

        var tileRenderable : ModelRenderable? = null
        var yWall : ModelRenderable? = null
        var xWall : ModelRenderable? = null

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

        for (wallIndex in board.xWalls) {
            if (wallIndex < (height + 1) * width) {
                val wall = Node()
                val x = 0f - ((width * size - size) / 2) + (wallIndex.rem(width) * size)
                val y = 0f - ((height * size) / 2) + (wallIndex.div(height) * size)
                wall.setParent(base)
                wall.renderable = xWall
                wall.localPosition = Vector3(x, size/2, y)
            }
        }

        for (wallIndex in board.yWalls) {
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
