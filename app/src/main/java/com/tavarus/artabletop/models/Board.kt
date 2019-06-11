package com.tavarus.artabletop.models

data class Board (
    val yWalls: List<Long> = listOf(),
    val xWalls: List<Long> = listOf(),
    val width: Long = -1,
    val height: Long = -1,
    val title: String = "",
    val id: Long = -1
)
