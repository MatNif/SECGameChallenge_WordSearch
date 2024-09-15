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
fun placeWordsInGrid(grid: MutableList<MutableList<GridCell>>, words: List<String>):
        MutableList<MutableList<GridCell>> {
    var setWords = mutableListOf<String>()
    var setWordLocations = mutableMapOf<String, List<GridCell>>()
    var setWordDirections = mutableMapOf<String, Int>()
    var wordIndex = 0

    while (setWords.size < words.size) {
        val word = words[wordIndex]
        var placed = false
        var attemptCounter = 0

        var wordToSet = word
        var anchorWord = ""
        var anchorCell = GridCell()

        // Keep trying to place the word until it fits in the grid
        while (!placed) {

            // Decide if the word should be anchored to an already placed word
            if (setWords.size > 0 && haveJointLetters(word, setWords).isNotEmpty()) {
                if (randomChance(chance = 0.8)) {
                    val wordsWithJointLetters = haveJointLetters(word, setWords)
                    anchorWord = wordsWithJointLetters.random()
                    val jointLetters = getJointLetters(word, anchorWord)
                    val randomJointLetter = jointLetters.random()
                    anchorCell = findLetterIndex(setWordLocations[anchorWord]!!, randomJointLetter)
                }
            }

            // Randomly choose the direction of the word
            val possibleDirections = mutableListOf(0, 1, 2, 3)
            if (setWordDirections.containsKey(anchorWord)) {
                possibleDirections.remove(setWordDirections[anchorWord])
            }
            val direction = possibleDirections.random()


            // Decide if word should be reversed
            if (randomChance(chance = 0.5)) {
                wordToSet = word.reversed()
            }

            // Place the word in the grid based on the direction
            val wordPlacing = preparePlacing(
                grid, wordToSet, direction, anchorCell,
                setWordLocations
            )

            // If the word can be placed, update the localGrid and setWordLocations
            if (wordPlacing.isValid) {
                // Replace the local grid cells with the word cells
                for (i in wordPlacing.location.indices) {
                    val cell = wordPlacing.location[i]
                    grid[cell.row][cell.col].letter = wordPlacing.word[i]
                }
                setWords.add(word)
                setWordLocations[word] = wordPlacing.location
                setWordDirections[word] = wordPlacing.direction
                placed = true // Exit the while loop
                wordIndex += 1
            } else {
                attemptCounter += 1
                if (attemptCounter >= 100) {
                    setWords = mutableListOf()
                    setWordLocations = mutableMapOf()
                    setWordDirections = mutableMapOf()
                    wordIndex = 0
                    break
                }
            }
        }
    }

    return grid
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
fun preparePlacing(grid: MutableList<MutableList<GridCell>>, word: String, direction: Int,
                   anchorCell: GridCell, setWordLocations: Map<String, List<GridCell>>):
        WordPlacing {

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
    var anchorLetterIndex = 0
    if (word.contains(localAnchorCell.letter)) {
        anchorLetterIndex = word.indexOf(localAnchorCell.letter)
    }

    // Calculate the placement of the word based on the direction and anchor cell
    val wordPlacing = calculateWordPlacing(word, direction, localAnchorCell, anchorLetterIndex, grid)
    if (wordPlacing.word.isEmpty()) {
        return wordPlacing
    }

    // Identify overlaps with previously set words
    for (wordLocation in setWordLocations.values) {
        for (cell in wordPlacing.location) {
            if (wordLocation.contains(cell)) {
                setWordOverlaps.add(cell)
                break
            }
        }
    }

    // Check if the letters in the word match the letters of the previously set words on overlap positions
    for (cell in setWordOverlaps) {
        val letterIndex = wordPlacing.location.indexOf(cell)
        if (wordPlacing.word[letterIndex] != cell.letter) {
            overlapMismatchFound = true
            break
        }
    }

    // If there are no overlaps or the overlaps match, update the WordPlacing object
    if (wordPlacing.word.isNotEmpty() && !overlapMismatchFound) {
        wordPlacing.isValid = true
    }

    return wordPlacing
}

// Calculate the placement of a word in the grid based on the direction and anchor cell
fun calculateWordPlacing(word: String, direction: Int, anchorCell: GridCell, anchorLetterIndex: Int,
                         grid: MutableList<MutableList<GridCell>>): WordPlacing {
    // Initialize the WordPlacing object. The default value is an unplaced word.
    var wordPlacing = WordPlacing()

    // Check if the word can be placed in the grid based on the direction and anchor cell
    when (direction) {
        0 -> {
            // Horizontal placement
            val row = anchorCell.row
            val startCol = anchorCell.col - anchorLetterIndex
            val endCol = startCol + word.length

            if (startCol >= 0 && endCol < grid[0].size) {
                val wordCells = mutableListOf<GridCell>()
                for (i in word.indices) {
                    wordCells.add(grid[row][startCol + i])
                }
                wordPlacing = WordPlacing(word, wordCells, direction)
            }
        }
        1 -> {
            // Vertical placement
            val col = anchorCell.col
            val startRow = anchorCell.row - anchorLetterIndex
            val endRow = startRow + word.length

            if (startRow >= 0 && endRow < grid.size) {
                val wordCells = mutableListOf<GridCell>()
                for (i in word.indices) {
                    wordCells.add(grid[startRow + i][col])
                }
                wordPlacing = WordPlacing(word, wordCells, direction)
            }
        }
        2 -> {
            // Diagonal (top-left to bottom-right) placement
            val startRow = anchorCell.row - anchorLetterIndex
            val startCol = anchorCell.col - anchorLetterIndex
            val endRow = startRow + word.length
            val endCol = startCol + word.length

            if (startRow >= 0 && startCol >= 0 && endRow < grid.size && endCol < grid[0].size) {
                val wordCells = mutableListOf<GridCell>()
                for (i in word.indices) {
                    wordCells.add(grid[startRow + i][startCol + i])
                }
                wordPlacing = WordPlacing(word, wordCells, direction)
            }
        }
        3 -> {
            // Diagonal (top-right to bottom-left) placement
            val startRow = anchorCell.row - anchorLetterIndex
            val startCol = anchorCell.col + anchorLetterIndex
            val endRow = startRow + word.length
            val endCol = startCol - word.length

            if (startRow >= 0 && startCol < grid[0].size && endRow < grid.size && endCol >= 0) {
                val wordCells = mutableListOf<GridCell>()
                for (i in word.indices) {
                    wordCells.add(grid[startRow + i][startCol - i])
                }
                wordPlacing = WordPlacing(word, wordCells, direction)
            }
        }
    }

    // If the word can be placed, update the WordPlacing object, else return the default 'unplaced' WordPlacing
    return wordPlacing
}
