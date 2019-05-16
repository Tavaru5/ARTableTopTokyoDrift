package com.tavarus.artabletop

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ux.ArFragment


class MainActivity : AppCompatActivity() {

    private var arFragment: ArFragment? = null
    private var board: Board? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?

        board = Board(6, 6, 0.15f, arrayOf(0, 1, 2, 6, 7, 21, 22, 32, 33, 33, 34),
            arrayOf(13, 14, 15, 16, 18, 19, 20, 33, 34) ,this)

        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment!!.arSceneView.scene)

            // Create the board and add it to the anchor.
            val boardNode = board!!.boardNode()
            anchorNode.addChild(boardNode)
        }


    }
}
