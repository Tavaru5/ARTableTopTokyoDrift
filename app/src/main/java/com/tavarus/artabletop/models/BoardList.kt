package com.tavarus.artabletop.models

data class BoardList (
    val boards: Map<String, Board> = hashMapOf()
)
