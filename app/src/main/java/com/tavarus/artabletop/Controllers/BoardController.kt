package com.tavarus.artabletop.Controllers

import android.content.Context
import android.util.Log
import com.google.ar.sceneform.Node
import com.google.firebase.firestore.FirebaseFirestore
import com.tavarus.artabletop.MainActivity
import com.tavarus.artabletop.Objects.Board
import com.tavarus.artabletop.POJO.BoardListPOJO
import java.lang.Exception

class BoardController(){
    companion object {
        var INSTANCE: BoardController = BoardController()
    }

    var currentBoard: Board? = null

    var boardsList: BoardListPOJO? = null

    fun initialize(uid: String, context: Context, onSuccess: ()->Unit, onFailure: (Exception?)->Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val doc = firestore.collection("users").document(uid)
        doc.get().addOnSuccessListener { document ->
            if (document != null) {
                boardsList = document.toObject(BoardListPOJO::class.java)
                //Future work will go into determining which board to select; for now we're just using the first one in the array
                currentBoard = Board(boardsList!!.boards[0], context)
                onSuccess()
            } else {
                Log.d("KOG", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("KOG", "get failed with ", exception)
        }
    }

    fun getNode(): Node? {
        return currentBoard?.boardNode()
    }

    fun clear() {
        currentBoard = null
    }

}
