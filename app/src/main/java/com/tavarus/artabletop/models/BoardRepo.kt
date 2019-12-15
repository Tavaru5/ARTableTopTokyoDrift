package com.tavarus.artabletop.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tavarus.artabletop.dataModels.BoardList
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class BoardRepo @Inject constructor() {

    val boardList: BehaviorSubject<BoardList> = BehaviorSubject.create()

    fun loadBoards() {

        val firestore = FirebaseFirestore.getInstance()
        val doc = firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        doc.get().addOnSuccessListener { document ->
            if (document != null) {
                val tempBoardsList = document.toObject(BoardList::class.java)
                boardList.onNext(tempBoardsList!!)
            } else {
                Log.d("KOG", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("KOG", "get failed with ", exception)
        }
    }

    fun clearBoards() {
        boardList.onNext(BoardList())
    }
}
