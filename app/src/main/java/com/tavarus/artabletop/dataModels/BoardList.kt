package com.tavarus.artabletop.dataModels

data class BoardList (
    val boards: Map<String, Board> = hashMapOf()
)
