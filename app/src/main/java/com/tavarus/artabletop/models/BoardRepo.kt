package com.tavarus.artabletop.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class BoardRepo @Inject constructor() {

    var boardsList: BoardList?

    val observableData: PublishSubject<BoardList> = PublishSubject.create()

    init {
        boardsList = BoardList()
        loadBoards()
    }

    private fun loadBoards() {
        val firestore = FirebaseFirestore.getInstance()
        val doc = firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        doc.get().addOnSuccessListener { document ->
            if (document != null) {
                val tempBoardsList = document.toObject(BoardList::class.java)
                observableData.onNext(tempBoardsList!!)
                boardsList = tempBoardsList
            } else {
                Log.d("KOG", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("KOG", "get failed with ", exception)
        }
    }
}
