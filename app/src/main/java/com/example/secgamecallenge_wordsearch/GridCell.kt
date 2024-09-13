package com.example.secgamecallenge_wordsearch

import androidx.compose.ui.geometry.Offset

data class GridCell(
    var letter: Char = ' ',  // Default value for letter (e.g., a space or a placeholder character)
    val row: Int = 0,  // Default value for row
    val col: Int = 0,  // Default value for column
    val topLeft: Offset = Offset.Zero,  // Default value for topLeft position
    val bottomRight: Offset = Offset.Zero,  // Default value for bottomRight position
    var isSelected: Boolean = false  // Default value for isSelected
)
