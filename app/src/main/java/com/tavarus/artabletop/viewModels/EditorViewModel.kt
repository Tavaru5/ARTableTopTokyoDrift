package com.tavarus.artabletop.viewModels

import androidx.lifecycle.MutableLiveData
import com.tavarus.artabletop.dataModels.MaterialEnum
import com.tavarus.artabletop.dataModels.NewBoard
import com.tavarus.artabletop.dataModels.Tile
import com.tavarus.artabletop.models.*
import javax.inject.Inject

class EditorViewModel @Inject constructor(val boardRepo: BoardRepo, val navState: NavState) {

    val board: MutableLiveData<NewBoard> = MutableLiveData()

    init {
        val tempBoard = NewBoard()
        val emptyTile = Tile(
            MaterialEnum.NONE,
            MaterialEnum.NONE,
            MaterialEnum.NONE,
            MaterialEnum.NONE,
            MaterialEnum.NONE
        )
        val emptyRow = listOf(emptyTile, emptyTile, emptyTile)
        tempBoard.tiles = listOf(emptyRow, emptyRow, emptyRow)
        board.value = tempBoard
    }

}
