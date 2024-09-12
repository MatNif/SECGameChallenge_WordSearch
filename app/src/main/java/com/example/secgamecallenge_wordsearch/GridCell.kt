package com.example.secgamecallenge_wordsearch

import androidx.compose.ui.geometry.Offset

data class GridCell(
    var letter: Char,
    val row: Int,
    val col: Int,
    val topLeft: Offset,
    val bottomRight: Offset,
    var isSelected: Boolean = false
)
