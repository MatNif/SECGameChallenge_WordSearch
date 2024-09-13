package com.example.secgamecallenge_wordsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.pointer.pointerInput

val wordsToFind = listOf("KOTLIN", "ANDROID", "COMPOSE", "VIEW", "LAYOUT")
val gridOrigin = Offset(0f, 0f)
val cellDimensions = 30f

@Composable
fun WordSearchGame(modifier: Modifier = Modifier) {
    val grid = remember { generateGrid(10, 10, gridOrigin, cellDimensions) }
    var foundWords by remember { mutableStateOf(emptyList<String>()) }
    var selectedCells by remember { mutableStateOf(listOf<GridCell>()) }
    var currentWord by remember { mutableStateOf("") } // Track the current word

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Word Search Game", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Drag gesture handling now at the grid level
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            selectedCells = emptyList() // Clear previous selection on new drag
                        },
                        onDragEnd = {
                            // Check if the selected word is valid
                            if (wordsToFind.contains(currentWord) && !foundWords.contains(currentWord)) {
                                foundWords = foundWords + currentWord // Add the word to found words
                            }
                            selectedCells = emptyList() // Clear the selected cells after checking
                        },
                        onDrag = { change, _ ->
                            val dragPosition = change.position
                            grid.flatten().forEach { cell ->
                                // Check if drag position is within cell boundaries
                                if (isInCellBounds(cell, dragPosition)) {

                                    if (!selectedCells.contains(cell)) {
                                        selectedCells = selectedCells + cell
                                        currentWord = selectedCells.map { it.letter }.joinToString("")
                                    }
                                    // Check if the selection is in a straight line
                                    if (selectedCells.size > 2) {
                                        if (!isSelectionStraight(selectedCells)) {
                                            selectedCells = straightenSelection(selectedCells, grid) // Remove the last cell
                                        }
                                    }

                                }
                            }
                        }
                    )
                }
        ) {
            WordSearchGrid(
                grid = grid,
                selectedCells = selectedCells
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display found words
        Text(text = "Found Words: ${foundWords.joinToString(", ")}")
    }
}

@Composable
fun WordSearchGrid(
    grid: List<List<GridCell>>,
    selectedCells: List<GridCell>
) {
    Column {
        for (row in grid) {
            Row {
                for (cell in row) {
                    WordSearchGridCell(
                        cell = cell,
                        selectedCells = selectedCells
                    )
                }
            }
        }
    }
}

@Composable
fun WordSearchGridCell(
    cell: GridCell,
    selectedCells: List<GridCell>
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(
                if (selectedCells.any { it.row == cell.row && it.col == cell.col }) Color.Gray else Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = cell.letter.toString())
    }
}
