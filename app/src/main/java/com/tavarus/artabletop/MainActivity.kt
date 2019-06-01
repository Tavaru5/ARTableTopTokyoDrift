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
import com.google.firebase.firestore.FirebaseFirestore
import com.tavarus.artabletop.Objects.Board
import com.tavarus.artabletop.POJO.BoardListPOJO
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

        if (FirebaseAuth.getInstance().currentUser == null) {
            unauthedUI()
        } else {
            authedUI()
            fetchUser()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                authedUI()
                fetchUser()
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
        //(That's the little phone that moves around)
        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, _: Plane, _: MotionEvent ->
            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment!!.arSceneView.scene)
            // Create the board and add it to the anchor.
            val boardNode = board!!.boardNode()
            anchorNode.addChild(boardNode)
        }
    }

    fun unauthedUI() {
        arFragment!!.planeDiscoveryController.hide()
        signInButton.visibility = View.VISIBLE
        settingsButton.visibility = View.GONE
        arFragment!!.planeDiscoveryController.setInstructionView(null)
    }

    fun fetchUser() {

        val firestore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val doc = firestore.collection("users").document(user!!.uid)
        doc.get().addOnSuccessListener { document ->
            if (document != null) {
                board = Board(
                    0.05f,
                    document.toObject(BoardListPOJO::class.java)!!.boards[0],
                    this
                )
            } else {
                Log.d("KOG", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("KOG", "get failed with ", exception)
        }
    }
}
