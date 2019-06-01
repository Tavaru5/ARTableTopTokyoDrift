package com.tavarus.artabletop.POJO

data class BoardPOJO (
    val yWalls: List<Long> = listOf(),
    val xWalls: List<Long> = listOf(),
    val width: Long = -1,
    val height: Long = -1
)