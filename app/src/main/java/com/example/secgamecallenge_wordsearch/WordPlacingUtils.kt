package com.example.secgamecallenge_wordsearch

import kotlin.random.Random

// Function to determine if a word should be reversed and find its anchor cell for placement
fun getWordAndAnchorCell(
    word: String,
    setWords: List<String>,
    setWordLocations: Map<String, List<GridCell>>
): Pair<String, GridCell> {
    val wordToSet = if (randomChance(0.5)) word.reversed() else word
    var anchorCell = GridCell()

    if (setWords.isNotEmpty() && haveJointLetters(word, setWords).isNotEmpty()) {
        if (randomChance(0.8)) {
            val anchorWord = haveJointLetters(word, setWords).random()
            val anchorLetter = getJointLetters(word, anchorWord).random()
            anchorCell = findLetterIndex(setWordLocations[anchorWord]!!, anchorLetter)
        }
    }
    return Pair(wordToSet, anchorCell)
}

// Function to get a random direction for word placement
fun getRandomDirection(
    setWordDirections: Map<String, Int>,
    anchorCell: GridCell
): Int {
    val possibleDirections = mutableListOf(0, 1, 2, 3)
    setWordDirections[anchorCell.letter.toString()]?.let { possibleDirections.remove(it) }
    return possibleDirections.random()
}

// Function to update the grid with a placed word
fun updateGridWithWord(
    grid: MutableList<MutableList<GridCell>>,
    wordPlacement: WordPlacement
) {
    wordPlacement.location.forEachIndexed { index, cell ->
        grid[cell.row][cell.col].letter = wordPlacement.word[index]
    }
}

// Function to reset all word placements in the grid
fun resetAllPlacements(
    setWords: MutableList<String>,
    setWordLocations: MutableMap<String, List<GridCell>>,
    setWordDirections: MutableMap<String, Int>
) {
    setWords.clear()
    setWordLocations.clear()
    setWordDirections.clear()
}

// Check if a word has joint letters with other words from a list
fun haveJointLetters(word: String, words: List<String>): List<String> {
    val wordsWithJointLetters = mutableListOf<String>()
    for (w in words) {
        for (i in word.indices) {
            if (w.contains(word[i])) {
                wordsWithJointLetters.add(w)
                break
            }
        }
    }
    return wordsWithJointLetters
}

// Check which the joint letters are between two words
fun getJointLetters(word1: String, word2: String): List<Char> {
    val jointLetters = mutableListOf<Char>()
    for (i in word1.indices) {
        if (word2.contains(word1[i])) {
            jointLetters.add(word1[i])
        }
    }
    return jointLetters
}

// Find the index of a letter in a list of GridCell objects
fun findLetterIndex(cells: List<GridCell>, letter: Char): GridCell {
    for (i in cells.indices) {
        if (cells[i].letter == letter) {
            return cells[i]
        }
    }
    return GridCell()
}

// Place a word in the grid based on the direction and anchor cell
fun preparePlacement(
    grid: MutableList<MutableList<GridCell>>,
    word: String,
    direction: Int,
    anchorCell: GridCell,
    setWordLocations: Map<String, List<GridCell>>
): WordPlacement {

    var localAnchorCell = anchorCell
    val setWordOverlaps = mutableListOf<GridCell>()
    var overlapMismatchFound = false

    // If no anchor cell is provided, set a random anchor point
    if (anchorCell.letter == ' ') {
        do {
            val randomRow = Random.nextInt(0, grid.size)
            val randomCol = Random.nextInt(0, grid[0].size)
            localAnchorCell = grid[randomRow][randomCol]
        } while (setWordLocations.values.flatten().contains(localAnchorCell))
    }

    // Check if the localAnchorCell holds a letter that appears in the word and return its index
    val anchorLetterIndex = word.indexOf(localAnchorCell.letter).takeIf { it >= 0 } ?: 0

    // Calculate the placement of the word based on the direction and anchor cell
    val wordPlacement = calculateWordPlacement(word, direction, localAnchorCell, anchorLetterIndex, grid)
    if (wordPlacement.word.isEmpty()) return wordPlacement

    // Identify overlaps with previously set words
    setWordLocations.values.flatten().forEach { cell ->
        if (wordPlacement.location.contains(cell)) setWordOverlaps.add(cell)
    }

    // Check if the letters in the word match the letters of the previously set words on overlap positions
    setWordOverlaps.forEach { cell ->
        val letterIndex = wordPlacement.location.indexOf(cell)
        if (wordPlacement.word[letterIndex] != cell.letter) {
            overlapMismatchFound = true
            return@forEach
        }
    }

    // If there are no overlaps or the overlaps match, update the WordPlacement object
    if (!overlapMismatchFound) wordPlacement.isValid = true

    return wordPlacement
}

// Calculate the placement of a word in the grid based on the direction and anchor cell
fun calculateWordPlacement(word: String, direction: Int, anchorCell: GridCell, anchorLetterIndex: Int,
                           grid: MutableList<MutableList<GridCell>>): WordPlacement {
    // Initialize the WordPlacement object. The default value is an unplaced word.
    var wordPlacement = WordPlacement()

    // Check if the word can be placed in the grid based on the direction and anchor cell
    when (direction) {
        0 -> {
            // Horizontal placement
            val row = anchorCell.row
            val startCol = anchorCell.col - anchorLetterIndex
            val endCol = startCol + word.length - 1

            if (startCol >= 0 && endCol < grid[0].size) {
                val wordCells = mutableListOf<GridCell>()
                for (i in word.indices) {
                    wordCells.add(grid[row][startCol + i])
                }
                wordPlacement = WordPlacement(word, wordCells, direction)
            }
        }
        1 -> {
            // Vertical placement
            val col = anchorCell.col
            val startRow = anchorCell.row - anchorLetterIndex
            val endRow = startRow + word.length - 1

            if (startRow >= 0 && endRow < grid.size) {
                val wordCells = mutableListOf<GridCell>()
                for (i in word.indices) {
                    wordCells.add(grid[startRow + i][col])
                }
                wordPlacement = WordPlacement(word, wordCells, direction)
            }
        }
        2 -> {
            // Diagonal (top-left to bottom-right) placement
            val startRow = anchorCell.row - anchorLetterIndex
            val startCol = anchorCell.col - anchorLetterIndex
            val endRow = startRow + word.length - 1
            val endCol = startCol + word.length - 1

            if (startRow >= 0 && startCol >= 0 && endRow < grid.size && endCol < grid[0].size) {
                val wordCells = mutableListOf<GridCell>()
                for (i in word.indices) {
                    wordCells.add(grid[startRow + i][startCol + i])
                }
                wordPlacement = WordPlacement(word, wordCells, direction)
            }
        }
        3 -> {
            // Diagonal (top-right to bottom-left) placement
            val startRow = anchorCell.row - anchorLetterIndex
            val startCol = anchorCell.col + anchorLetterIndex
            val endRow = startRow + word.length - 1
            val endCol = startCol - word.length + 1

            if (startRow >= 0 && startCol < grid[0].size && endRow < grid.size && endCol >= 0) {
                val wordCells = mutableListOf<GridCell>()
                for (i in word.indices) {
                    wordCells.add(grid[startRow + i][startCol - i])
                }
                wordPlacement = WordPlacement(word, wordCells, direction)
            }
        }
    }

    // If the word can be placed, update the WordPlacement object, else return the default 'unplaced' WordPlacement
    return wordPlacement
}

