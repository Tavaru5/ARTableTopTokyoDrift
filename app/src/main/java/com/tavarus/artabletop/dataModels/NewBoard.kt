package com.tavarus.artabletop.dataModels

data class NewBoard(
    var tiles: List<List<List<Tile>>> = listOf(),
    var title: String = ""
) {
    fun height(): Int {
        return if (tiles.isEmpty()) 0 else tiles[0].size
    }

    fun width(): Int {
        return if (tiles.isEmpty() or tiles[0].isEmpty()) 0 else tiles[0][0].size
    }

    fun floors(): Int {
        return tiles.size
    }
}
