package com.tavarus.artabletop.models

data class NewBoard(
    var tiles: List<List<Tile>> = listOf(listOf()),
    var title: String = ""
) {
    fun height(): Int {
        return tiles.size
    }

    fun width(): Int {
        return if (tiles.isEmpty()) 0 else tiles[0].size
    }
}
