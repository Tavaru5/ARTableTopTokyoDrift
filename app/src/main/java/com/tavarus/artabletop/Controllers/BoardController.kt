package com.tavarus.artabletop.Controllers

import android.content.Context
import com.google.ar.sceneform.Node
import com.tavarus.artabletop.Objects.Board
import com.tavarus.artabletop.POJO.BoardListPOJO

class BoardController(){
    companion object {
        var INSTANCE: BoardController = BoardController()
    }

    var currentBoard: Board? = null

    fun initialize(list: BoardListPOJO, context: Context) {
        currentBoard = Board(0.05f, list.boards[0], context)
    }

    fun getNode(): Node? {
        return currentBoard?.boardNode()
    }

    fun clear() {
        currentBoard = null
    }

}