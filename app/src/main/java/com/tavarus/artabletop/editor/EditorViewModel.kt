package com.tavarus.artabletop.editor

import androidx.lifecycle.MutableLiveData
import com.tavarus.artabletop.dataModels.MaterialEnum
import com.tavarus.artabletop.dataModels.Board
import com.tavarus.artabletop.dataModels.Tile
import com.tavarus.artabletop.editor.bottomSheet.TrayItem
import com.tavarus.artabletop.models.*
import javax.inject.Inject

typealias TrayCallBack = () -> Unit

class EditorViewModel @Inject constructor(val boardRepo: BoardRepo, val navState: NavState) {

    val board: MutableLiveData<Board> = MutableLiveData()
    val trayItems: MutableLiveData<List<Pair<TrayItem, TrayCallBack>>> = MutableLiveData<List<Pair<TrayItem, TrayCallBack>>>()
        .apply{ postValue(listOf(
            Pair(TrayItem.SAVE, {}),
            Pair(TrayItem.PALETTE, {})
        ))}

    val selectedMaterial: MutableLiveData<MaterialEnum> = MutableLiveData<MaterialEnum>()
        .apply { postValue(MaterialEnum.STONE) }

    init {
        board.value = Board(
            listOf(
                MutableList(20) {
                    MutableList(20) {
                            Tile(
                                MaterialEnum.NONE,
                                MaterialEnum.NONE,
                                MaterialEnum.NONE,
                                MaterialEnum.NONE,
                                MaterialEnum.NONE
                            )
                    }
                }
            )
        )
    }
}
