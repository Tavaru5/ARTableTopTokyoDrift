package com.tavarus.artabletop.viewModels

import androidx.lifecycle.MutableLiveData
import com.tavarus.artabletop.models.Board
import com.tavarus.artabletop.models.BoardRepo
import com.tavarus.artabletop.models.NavState
import javax.inject.Inject

class EditorViewModel @Inject constructor(val boardRepo: BoardRepo, val navState: NavState) {

    val board: MutableLiveData<Board> = MutableLiveData()

    init {
        val tempBoard = Board()
        tempBoard.height = 4
        tempBoard.width = 4
        board.value = tempBoard
    }

}
