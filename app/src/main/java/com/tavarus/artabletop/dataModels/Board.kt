package com.tavarus.artabletop.dataModels

data class Board (
    val yWalls: List<Int> = listOf(),
    val xWalls: List<Int> = listOf(),
    var width: Int = -1,
    var height: Int = -1,
    var title: String = ""
)