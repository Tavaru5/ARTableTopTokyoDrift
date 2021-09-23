package com.tavarus.artabletop.dataModels

data class Tile(
    var material: MaterialEnum = MaterialEnum.NONE,
    var leftWall: MaterialEnum = MaterialEnum.NONE,
    var rightWall: MaterialEnum = MaterialEnum.NONE,
    var topWall: MaterialEnum = MaterialEnum.NONE,
    var bottomWall: MaterialEnum = MaterialEnum.NONE,
) {
    fun isEmpty() = material == MaterialEnum.NONE &&
                leftWall == MaterialEnum.NONE &&
                rightWall == MaterialEnum.NONE &&
                topWall == MaterialEnum.NONE &&
                bottomWall == MaterialEnum.NONE
}
