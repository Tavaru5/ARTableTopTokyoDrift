package com.tavarus.artabletop.dataModels

data class BoardFirebaseObject(var tiles: List<BFORows> = listOf(), var title: String = "")

data class BFORows(val rows: List<BFOColumns> = listOf())

data class BFOColumns(val columns: List<Tile> = listOf())

data class BFOList(val boards: List<BoardFirebaseObject> = listOf())

fun BoardFirebaseObject.toBoard() = Board(
    title = title,
    tiles = tiles.map { bfoRows ->
        bfoRows.rows.map { bfoColumns ->
            bfoColumns.columns
        }
    }
)

fun BFOList.toBoardList() = BoardList(
    boards.map { it.toBoard() }
)
