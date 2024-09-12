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

        // Pass grid, selectedCells, and foundWords to the grid
        WordSearchGrid(
            grid = grid,
            selectedCells = selectedCells,
            updateSelectedCells = { selectedCells = it }, // Update selectedCells when cells are selected
            updateCurrentWord = { word -> currentWord = word }, // Update current word during drag
            onDragEnd = {
                // Check if the selected word is valid
                if (wordsToFind.contains(currentWord) && !foundWords.contains(currentWord)) {
                    foundWords = foundWords + currentWord // Add the word to found words
                }
                selectedCells = emptyList() // Clear the selected cells after checking
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display found words
        Text(text = "Found Words: ${foundWords.joinToString(", ")}")
    }
}

@Composable
fun WordSearchGrid(
    grid: List<List<GridCell>>,
    selectedCells: List<GridCell>,
    updateSelectedCells: (List<GridCell>) -> Unit,
    updateCurrentWord: (String) -> Unit,
    onDragEnd: () -> Unit
) {
    Column {
        for (row in grid) {
            Row {
                for (cell in row) {
                    WordSearchGridCell(
                        cell = cell,
                        selectedCells = selectedCells,
                        updateSelectedCells = updateSelectedCells,
                        updateCurrentWord = updateCurrentWord,
                        onDragEnd = onDragEnd
                    )
                }
            }
        }
    }
}

@Composable
fun WordSearchGridCell(
    cell: GridCell,
    selectedCells: List<GridCell>,
    updateSelectedCells: (List<GridCell>) -> Unit,
    updateCurrentWord: (String) -> Unit,
    onDragEnd: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(
                if (selectedCells.any { it.row == cell.row && it.col == cell.col }) Color.Gray else Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        if (!selectedCells.contains(cell)) {
                            updateSelectedCells(selectedCells + cell)
                            updateCurrentWord((selectedCells + cell).map { it.letter }.joinToString(""))
                        }
                    },
                    onDragEnd = {
                        onDragEnd() // Trigger validation of the selected word when dragging ends
                    },
                    onDrag = { change, _ ->
                        val dragPosition = change.position  // Get the current drag position

                        // Check if the drag position is within the current cell's boundaries
                        if (dragPosition.x >= cell.topLeft.x && dragPosition.x <= cell.bottomRight.x &&
                            dragPosition.y >= cell.topLeft.y && dragPosition.y <= cell.bottomRight.y) {

                            // Add the cell to the selected list if not already selected
                            if (!selectedCells.any { it.row == cell.row && it.col == cell.col }) {
                                updateSelectedCells(selectedCells + cell)
                                updateCurrentWord((selectedCells + cell).map { it.letter }.joinToString(""))
                            }
                        }
                    }
                )
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = cell.letter.toString())
    }
}

