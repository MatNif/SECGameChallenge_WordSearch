package com.example.secgamecallenge_wordsearch

data class GridCell(
    var letter: Char,
    var isSelected: Boolean = false
)