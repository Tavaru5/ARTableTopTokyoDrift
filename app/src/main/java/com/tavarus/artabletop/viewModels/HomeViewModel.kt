package com.tavarus.artabletop.viewModels

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.tavarus.artabletop.models.BoardList
import com.tavarus.artabletop.models.BoardRepo
import com.tavarus.artabletop.models.NavState
import com.tavarus.artabletop.models.NavStateEnum
import javax.inject.Inject

class HomeViewModel @Inject constructor(val boardRepo: BoardRepo, val navState: NavState) {

    val boardsList: MutableLiveData<BoardList> = MutableLiveData()

    init {
        boardsList.value = BoardList()
        boardRepo.boardList.subscribe { boards ->
            boardsList.value = boards
        }

        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                navState.pushToView(NavStateEnum.LOGIN, false)
            } else {
                boardRepo.loadBoards()
            }
        }
    }

    fun goToBoard(id: String) {
        navState.selectedBoardID = id
        navState.pushToView(NavStateEnum.BOARD, true)
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        boardRepo.clearBoards()
    }

    fun goToAddBoard() {
        navState.pushToView(NavStateEnum.EDITOR)
    }

}
