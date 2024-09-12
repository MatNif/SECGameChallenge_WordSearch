package com.example.secgamecallenge_wordsearch

import kotlin.random.Random
import androidx.compose.ui.geometry.Offset

// Function to generate a 10x10 grid of GridCell objects with random letters
fun generateGrid(rows: Int, cols: Int, gridOrigin: Offset, cellDimension: Float): MutableList<MutableList<GridCell>> {
    // Generate the grid and assign row and column positions
    val grid = MutableList(rows) { row ->
        MutableList(cols) { col ->
            val randomLetter = ('A'..'Z').random()  // Random letter generation
            val topLeft = Offset(gridOrigin.x + col * cellDimension, gridOrigin.y + row * cellDimension)
            val bottomRight = Offset(gridOrigin.x + (col + 1) * cellDimension, gridOrigin.y + (row + 1) * cellDimension)
            GridCell(letter = randomLetter, row = row, col = col, topLeft = topLeft, bottomRight = bottomRight)  // Include row and col in the GridCell
        }
    }

    // Insert the words into the grid
    placeWordsInGrid(grid, wordsToFind)

    return grid
}

// Helper function to insert words into the grid at random positions
fun placeWordsInGrid(grid: MutableList<MutableList<GridCell>>, words: List<String>) {
    for (word in words) {
        var placed = false

        // Keep trying to place the word until it fits in the grid
        while (!placed) {
            val direction = Random.nextInt(0, 3)  // 0 = horizontal, 1 = vertical, 2 = diagonal
            val row = Random.nextInt(grid.size)
            val col = Random.nextInt(grid[0].size)

            when (direction) {
                0 -> placed = placeWordHorizontally(grid, word, row, col)
                1 -> placed = placeWordVertically(grid, word, row, col)
                2 -> placed = placeWordDiagonally(grid, word, row, col)
            }
        }
    }
}

// Place a word horizontally if it fits, otherwise return false
fun placeWordHorizontally(grid: MutableList<MutableList<GridCell>>, word: String, row: Int, col: Int): Boolean {
    if (col + word.length > grid[0].size) return false
    for (i in word.indices) {
        grid[row][col + i].letter = word[i]
    }
    return true
}

// Place a word vertically if it fits, otherwise return false
fun placeWordVertically(grid: MutableList<MutableList<GridCell>>, word: String, row: Int, col: Int): Boolean {
    if (row + word.length > grid.size) return false
    for (i in word.indices) {
        grid[row + i][col].letter = word[i]
    }
    return true
}

// Place a word diagonally (top-left to bottom-right) if it fits, otherwise return false
fun placeWordDiagonally(grid: MutableList<MutableList<GridCell>>, word: String, row: Int, col: Int): Boolean {
    if (row + word.length > grid.size || col + word.length > grid[0].size) return false
    for (i in word.indices) {
        grid[row + i][col + i].letter = word[i]
    }
    return true
}
