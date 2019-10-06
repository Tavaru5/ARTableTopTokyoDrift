package com.tavarus.artabletop.viewModels

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.tavarus.artabletop.models.BoardList
import com.tavarus.artabletop.models.BoardRepo
import com.tavarus.artabletop.models.NavState
import com.tavarus.artabletop.models.NavStateEnum
import javax.inject.Inject

class HomeViewModel @Inject constructor(val boardRepo: BoardRepo, val navState: NavState) {

    val boardsList: MutableLiveData<BoardList> = MutableLiveData()

    init {
        boardsList.value = BoardList()
        //Observe changes to boardRepo
        boardRepo.observableData.subscribe {boards ->
            boardsList.value = boards
        }
    }

    fun navigateToBoard(id: String) {
        val args = Bundle()
        args.putString("ID", id)
        navState.pushToView(NavStateEnum.BOARD, args)
    }

}
