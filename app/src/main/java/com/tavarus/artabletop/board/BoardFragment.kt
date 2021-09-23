package com.tavarus.artabletop.board

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
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
import com.tavarus.artabletop.dataModels.MaterialEnum
import com.tavarus.artabletop.dataModels.Board
import javax.inject.Inject

class BoardFragment : ArFragment() {


    var canPlace: Boolean = false

    @Inject
    lateinit var boardViewModel: BoardViewModel

    override fun onAttach(context: Context) {
        val coreComponent = (activity?.applicationContext as App).provideCoreComponent()
        coreComponent.componentManager().getOrCreateBoardComponent(activity?.applicationContext as Context, coreComponent).inject(this)
        super.onAttach(context)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardObserver = Observer<Board> { newBoard ->
            canPlace = (newBoard != null)
            // TODO: Hide/show loading
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

        val height = board.height()
        val width = board.width()

        val tileSeparation = 0.04f
        val size = 0.05f

        var tileRenderable : ModelRenderable? = null
        var verticalWall : ModelRenderable? = null
        var horizontalWall : ModelRenderable? = null

        MaterialFactory.makeOpaqueWithColor(context, com.google.ar.sceneform.rendering.Color(Color.BLACK))
            .thenAccept { material ->
                tileRenderable = ShapeFactory.makeCube(
                    Vector3(
                        (1f - tileSeparation / 2) * size,
                        0.01f,
                        (1f - tileSeparation / 2) * size),
                    Vector3(0f,0f,0f),
                    material)
                horizontalWall = ShapeFactory.makeCube(Vector3(size, size , tileSeparation * size), Vector3(0f,0f,0f),material)
                verticalWall = ShapeFactory.makeCube(Vector3(tileSeparation * size, size , size), Vector3(0f,0f,0f),material)

            }
        val base = Node()
        base.localPosition = Vector3(0f, 0.2f, 0f)
        board.tiles.forEachIndexed { fi, row ->
            row.forEachIndexed { ri, column ->
                column.forEachIndexed { ci, tile ->
                    val x = 0f - ((width * size - size) / 2) + (ci * size)
                    val y = 0f - ((height * size - size) / 2) + (ri * size)
                    when (tile.material) {
                        MaterialEnum.STONE -> {
                            val tileNode = Node()
                            tileNode.setParent(base)
                            tileNode.renderable = tileRenderable
                            tileNode.localPosition = Vector3(x, 0f, y)
                        }
                        MaterialEnum.WOOD,
                        MaterialEnum.NONE -> {}
                    }
                    when (tile.leftWall) {
                        MaterialEnum.STONE -> {
                            val wallNode = Node()
                            wallNode.setParent(base)
                            wallNode.renderable = verticalWall
                            wallNode.localPosition = Vector3(x - size/2, size/2, y)
                        }
                        MaterialEnum.WOOD,
                        MaterialEnum.NONE -> {}
                    }
                    when (tile.topWall) {
                        MaterialEnum.STONE -> {
                            val wallNode = Node()
                            wallNode.setParent(base)
                            wallNode.renderable = horizontalWall
                            wallNode.localPosition = Vector3(x, size/2, y - size/2)
                        }
                        MaterialEnum.WOOD,
                        MaterialEnum.NONE -> {}
                    }
                    when (tile.rightWall) {
                        MaterialEnum.STONE -> {
                            val wallNode = Node()
                            wallNode.setParent(base)
                            wallNode.renderable = verticalWall
                            wallNode.localPosition = Vector3(x + size/2, size/2, y)
                        }
                        MaterialEnum.WOOD,
                        MaterialEnum.NONE -> {}
                    }
                    when (tile.bottomWall) {
                        MaterialEnum.STONE -> {
                            val wallNode = Node()
                            wallNode.setParent(base)
                            wallNode.renderable = horizontalWall
                            wallNode.localPosition = Vector3(x, size/2, y + size/2)
                        }
                        MaterialEnum.WOOD,
                        MaterialEnum.NONE -> {}
                    }
                }
            }
        }
        return base
    }
}
