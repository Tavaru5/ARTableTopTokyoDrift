package com.tavarus.artabletop.viewModels

import androidx.lifecycle.MutableLiveData
import com.tavarus.artabletop.models.Board
import com.tavarus.artabletop.models.BoardRepo
import com.tavarus.artabletop.models.NavState
import javax.inject.Inject

class BoardViewModel @Inject constructor(boardRepo: BoardRepo, val navState: NavState) {

    val board: MutableLiveData<Board> = MutableLiveData()

    init {
        board.value = Board()
        boardRepo.boardList.subscribe { boards ->
            board.value = boards.boards[navState.selectedBoardID]
        }
    }

}
