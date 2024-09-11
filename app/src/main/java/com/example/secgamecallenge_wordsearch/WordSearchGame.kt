package com.example.secgamecallenge_wordsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

val wordsToFind = listOf("KOTLIN", "ANDROID", "COMPOSE", "VIEW", "LAYOUT")
var lastSelectedCell: GridCell? = null  // Keep track of the last selected cell

@Composable
fun WordSearchGame(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf(TextFieldValue("")) }
    val grid = remember { generateGrid(10, 10) }
    var foundWords by remember { mutableStateOf(emptyList<String>()) }

    // Win condition: Check if all words are found
    val allWordsFound = remember(foundWords) {
        foundWords.containsAll(wordsToFind)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Word Search Game", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // If all words are found, show the congratulatory message
        if (allWordsFound) {
            Text(
                text = "Congratulations! You found all the words!",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Green
            )
        } else {
            BasicTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Display the word search grid
            Column {
                for (row in grid) {
                    Row {
                        for (cell in row) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(
                                        if (cell.isSelected) Color.Gray else Color.White,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .clickable {
                                        // Check if the cell is adjacent to the last selected cell
                                        if (lastSelectedCell == null || isAdjacent(
                                                lastSelectedCell!!, cell
                                            )
                                        ) {
                                            cell.isSelected = !cell.isSelected
                                            lastSelectedCell =
                                                if (cell.isSelected) cell else null

                                            val selectedWord = grid.flatten()
                                                .filter { it.isSelected }
                                                .map { it.letter }
                                                .joinToString("")

                                            if (wordsToFind.contains(selectedWord) && !foundWords.contains(
                                                    selectedWord
                                                )
                                            ) {
                                                foundWords = foundWords + selectedWord
                                            }
                                        }
                                    }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = cell.letter.toString())
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display found words
        Text(text = "Found Words: ${foundWords.joinToString(", ")}")
    }
}


fun isWordInGrid(grid: List<List<GridCell>>, word: String): Boolean {
    val rows = grid.size
    val cols = grid[0].size

    // Check horizontally
    for (i in 0 until rows) {
        for (j in 0..(cols - word.length)) {
            if ((0 until word.length).all { k -> grid[i][j + k].letter == word[k] }) {
                return true
            }
        }
    }

    // Check vertically
    for (i in 0..(rows - word.length)) {
        for (j in 0 until cols) {
            if ((0 until word.length).all { k -> grid[i + k][j].letter == word[k] }) {
                return true
            }
        }
    }

    // Check diagonally (top-left to bottom-right)
    for (i in 0..(rows - word.length)) {
        for (j in 0..(cols - word.length)) {
            if ((0 until word.length).all { k -> grid[i + k][j + k].letter == word[k] }) {
                return true
            }
        }
    }

    // Check diagonally (bottom-left to top-right)
    for (i in (word.length - 1) until rows) {
        for (j in 0..(cols - word.length)) {
            if ((0 until word.length).all { k -> grid[i - k][j + k].letter == word[k] }) {
                return true
            }
        }
    }

    return false
}

