package com.tavarus.artabletop.dataModels

data class Board(
    var tiles: List<List<List<Tile>>> = listOf(),
    var title: String = ""
) {
    fun height(): Int {
        return if (tiles.isEmpty()) 0 else tiles[0].size
    }

    fun width(): Int {
        return if (tiles.isEmpty() || tiles[0].isEmpty()) 0 else tiles[0][0].size
    }

    fun floors(): Int {
        return tiles.size
    }
}

fun Board.toFirebaseObject() = BoardFirebaseObject(
    title = title,
    tiles = tiles.map { rows ->
        BFORows(rows.map { columns ->
            BFOColumns(columns)
        })
    }
)
