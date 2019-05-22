package com.tavarus.artabletop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ux.ArFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var arFragment: ArFragment? = null
    private var board: Board? = null

    private val RC_SIGN_IN = 516

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?

        signInButton.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), RC_SIGN_IN)
        }
        //Eventually this would be a full menu
        settingsButton.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener { unauthedUI() }
        }

        board = Board(6, 6, 0.05f, arrayOf(0, 1, 2, 6, 7, 21, 22, 32, 33, 33, 34),
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

        if (FirebaseAuth.getInstance().currentUser == null) {
            unauthedUI()
        } else {
            authedUI()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                //This is where we want to start getting the user's data, specifically saved boards
                val user = FirebaseAuth.getInstance().currentUser
                authedUI()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    fun authedUI() {
        arFragment!!.planeDiscoveryController.show()
        signInButton.visibility = View.GONE
        settingsButton.visibility = View.VISIBLE
        //It would be nice to see if we could add back the default planeDiscoveryController InstructionView
        //(That's the little phone that moves around
    }

    fun unauthedUI() {
        arFragment!!.planeDiscoveryController.hide()
        signInButton.visibility = View.VISIBLE
        settingsButton.visibility = View.GONE
        arFragment!!.planeDiscoveryController.setInstructionView(null)
    }
}
