package com.example.secgamecallenge_wordsearch

data class WordPlacement(
    val word: String = "",
    var location: List<GridCell> = mutableListOf(), // Default value for location
    var direction: Int = -1, // Integer value representing the direction of the word
    var isValid: Boolean = false, // Default value for isPlaced
)

// Direction encoding
// 0 - Horizontal
// 1 - Vertical
// 2 - Diagonal (top-left to bottom-right)
// 3 - Diagonal (top-right to bottom-left)
