package com.tavarus.artabletop.models

data class Tile(
    val material: MaterialEnum,
    val leftWall: MaterialEnum,
    val rightWall: MaterialEnum,
    val topWall: MaterialEnum,
    val bottomWall: MaterialEnum
)
