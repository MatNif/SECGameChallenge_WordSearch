package com.example.secgamecallenge_wordsearch

import kotlin.random.Random
import androidx.compose.ui.geometry.Offset

// Function to generate a 10x10 grid of GridCell objects with random letters
fun generateGrid(rows: Int, cols: Int, gridOrigin: Offset, cellDimension: Float):
        MutableList<MutableList<GridCell>> {

    // Generate the grid and assign row and column positions
    var grid = MutableList(rows) { row ->
        MutableList(cols) { col ->
            val randomLetter = ('A'..'Z').random()  // Random letter generation
            val topLeft = Offset(gridOrigin.x + col * cellDimension, gridOrigin.y + row * cellDimension)
            val bottomRight = Offset(gridOrigin.x + (col + 1) * cellDimension, gridOrigin.y + (row + 1) * cellDimension)
            GridCell(letter = randomLetter, row = row, col = col, topLeft = topLeft, bottomRight = bottomRight)  // Include row and col in the GridCell
        }
    }

    // Insert the words into the grid
    grid = placeWordsInGrid(grid, wordsToFind)

    return grid
}

// Helper function to insert words into the grid at random positions
fun placeWordsInGrid(
    grid: MutableList<MutableList<GridCell>>,
    words: List<String>
): MutableList<MutableList<GridCell>> {
    val setWords = mutableListOf<String>()
    val setWordLocations = mutableMapOf<String, List<GridCell>>()
    val setWordDirections = mutableMapOf<String, Int>()
    var wordIndex = 0

    while (setWords.size < words.size) {
        val word = words[wordIndex]
        var placed = false
        var attemptCounter = 0

        while (!placed && attemptCounter < 100) {
            val (wordToSet, anchorCell) = getWordAndAnchorCell(word, setWords, setWordLocations)
            val direction = getRandomDirection(setWordDirections, anchorCell)
            val wordPlacement = preparePlacement(grid, wordToSet, direction, anchorCell, setWordLocations)

            if (wordPlacement.isValid) {
                updateGridWithWord(grid, wordPlacement)
                setWords.add(word)
                setWordLocations[word] = wordPlacement.location
                setWordDirections[word] = wordPlacement.direction
                placed = true
                wordIndex += 1
            } else {
                attemptCounter += 1
                if (attemptCounter >= 100) {
                    resetAllPlacements(setWords, setWordLocations, setWordDirections)
                    wordIndex = 0
                }
            }
        }
    }
    return grid
}
