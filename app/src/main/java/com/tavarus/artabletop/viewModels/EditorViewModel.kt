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
        val width = (navState.screenParams["width"] ?: 4) as Int
        val height = (navState.screenParams["height"] ?: 4) as Int
        val emptyTile = Tile(
            MaterialEnum.NONE,
            MaterialEnum.NONE,
            MaterialEnum.NONE,
            MaterialEnum.NONE,
            MaterialEnum.NONE
        )
        val row = List(width) {emptyTile}
        tempBoard.tiles = listOf(List(height) {row})
        board.value = tempBoard
    }

}
