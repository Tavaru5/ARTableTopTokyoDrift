package com.tavarus.artabletop.viewModels

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tavarus.artabletop.CoreComponent
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

    fun navigateToBoard(id: Long) {
        val args = Bundle()
        args.putLong("ID", id)
        navState.pushToView(NavStateEnum.BOARD, args)
    }

}
