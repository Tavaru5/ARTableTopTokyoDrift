package com.tavarus.artabletop.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tavarus.artabletop.models.Board
import com.tavarus.artabletop.models.BoardList
import javax.inject.Inject

class BoardsViewModel @Inject constructor() {

    var currentBoard: MutableLiveData<Board> = MutableLiveData()

    val boardsList: MutableLiveData<BoardList> = MutableLiveData()

    init {
        boardsList.value = BoardList()
        loadBoards()
    }

    private fun loadBoards() {
        val firestore = FirebaseFirestore.getInstance()
        val doc = firestore.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        doc.get().addOnSuccessListener { document ->
            if (document != null) {
                boardsList.value = document.toObject(BoardList::class.java)
                if (boardsList.value?.boards?.isNotEmpty() == true) {
                    currentBoard.value = boardsList.value?.boards!![0]
                }
            } else {
                Log.d("KOG", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("KOG", "get failed with ", exception)
        }
    }

}

