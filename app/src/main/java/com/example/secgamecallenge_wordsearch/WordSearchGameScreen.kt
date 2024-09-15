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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.platform.LocalDensity


val wordsToFind = listOf("KOTLIN", "ANDROID", "COMPOSE", "VIEW", "LAYOUT")
val gridOrigin = Offset(0f, 0f)
val cellDimensions = 70.dp


@Composable
fun WordSearchGame(modifier: Modifier = Modifier, onGameOver: (Boolean, Int, Int) -> Unit) {
    // Layout values for the game screen
    val cellDimensionPx = with(LocalDensity.current) { cellDimensions.toPx() }
    val grid = remember { generateGrid(10, 10, gridOrigin, cellDimensionPx) }

    // Game state variables
    var foundWords by remember { mutableStateOf(emptyList<String>()) }
    var selectedCells by remember { mutableStateOf(listOf<GridCell>()) }
    var currentWord by remember { mutableStateOf("") } // Track the current word
    var timeLeft by remember { mutableStateOf(120) } // 2 minutes (120 seconds)

    // Start countdown timer
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            kotlinx.coroutines.delay(1000L)  // Delay for 1 second
            timeLeft--
        } else {
            // When the timer hits 0, trigger game over
            onGameOver(false, 120 - timeLeft, foundWords.size)
        }
    }

    // Check if player has found all words
    if (foundWords.size == 5) {
        onGameOver(true, 120 - timeLeft, foundWords.size)
    }

    // Layout for the game screen
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the word search grid on the left
        Column(
            modifier = Modifier.weight(3f),  // Adjust the width of the grid column
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SEC Game Changer - Word Search",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Drag gesture handling at the grid level
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
        }

        // Timer and words list panel on the right
        Column(
            modifier = Modifier.weight(1f),  // Adjust the width of the timer and word list column
            horizontalAlignment = Alignment.Start
        ) {
            // Timer display
            Text(
                text = "Time Left: ${timeLeft / 60}:${(timeLeft % 60).toString().padStart(2, '0')}",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(30.dp))

            // Display words to find
            Text(
                text = "Words to Find:",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                wordsToFind.forEach { word ->
                    Text(
                        text = word,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (foundWords.contains(word)) Color.LightGray else Color.Black,
                        fontSize = 28.sp,
                        textDecoration = if (foundWords.contains(word)) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
        }
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
            .size(cellDimensions)
            .background(
                if (selectedCells.any { it.row == cell.row && it.col == cell.col }) Color.Gray else Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = cell.letter.toString(), fontSize = 28.sp)
    }
}
