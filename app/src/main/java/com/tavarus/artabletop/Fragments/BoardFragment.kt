package com.tavarus.artabletop.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ux.ArFragment
import com.tavarus.artabletop.Controllers.AuthController
import com.tavarus.artabletop.Controllers.BoardController
import com.tavarus.artabletop.MainActivity
import com.tavarus.artabletop.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.board_fragment.*

class BoardFragment : ArFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnTapArPlaneListener { hitResult: HitResult, _: Plane, _: MotionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arSceneView.scene)
            anchorNode.addChild(BoardController.INSTANCE.getNode())
        }
    }
}
