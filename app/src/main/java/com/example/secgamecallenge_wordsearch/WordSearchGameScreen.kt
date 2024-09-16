package com.example.secgamecallenge_wordsearch

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity


@Composable
fun WordSearchGame(modifier: Modifier = Modifier, onGameOver: (Boolean, Int, Int) -> Unit) {
    // Get the words for the current day of the month
    val now = Calendar.getInstance()
    val dayOfMonth = now.get(Calendar.DAY_OF_MONTH)
    val wordsToFind = wordsForTheMonth[dayOfMonth] ?: emptyList()

    // Layout values for the game screen
    val cellDimensionPx = with(LocalDensity.current) { cellDimensions.toPx() }
    val grid = remember { generateGrid(10, 10, gridOrigin, cellDimensionPx, wordsToFind) }

    // Game state variables
    var foundWords by remember { mutableStateOf(emptyList<String>()) }
    var selectedCells by remember { mutableStateOf(listOf<GridCell>()) }
    var currentWord by remember { mutableStateOf("") } // Track the current word
    var timeLeft by remember { mutableStateOf(gameDuration) } // 2 minutes (120 seconds)
    var timeOfLastFind by remember { mutableStateOf(0) } // Time of the last word find

    // Start countdown timer
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            kotlinx.coroutines.delay(1000L)  // Delay for 1 second
            timeLeft--
        } else {
            // When the timer hits 0, trigger game over
            onGameOver(false, timeOfLastFind, foundWords.size)
        }
    }

    // Dynamic timer color based on time left
    val timerColor = when {
        timeLeft > 30 -> Color(0xFF00796B)  // Green (calm)
        timeLeft in 11..30 -> Color(0xFFFF9800)  // Orange (warning)
        else -> Color(0xFFD32F2F)  // Red (danger)
    }

    // Check if player has found all words
    if (foundWords.size == 5) {
        onGameOver(true, timeOfLastFind, foundWords.size)
    }

    // Handle hidden word (last word)
    val hiddenWord = wordsToFind.lastOrNull() ?: ""
    val displayedWords = wordsToFind.dropLast(1) + hiddenWord.map { '?' }.joinToString(" ")

    // Layout for the game screen
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
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
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004D40)  // Dark green accent
                ),
                fontSize = 28.sp
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
                                    timeOfLastFind = gameDuration - timeLeft // Update time of last find
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
                                        if (selectedCells.size > 2 && !isSelectionStraight(selectedCells)) {
                                            selectedCells = straightenSelection(selectedCells, grid) // Remove the last cell
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
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = timerColor,  // Dynamic timer color based on time left
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Display words to find
            Text(
                text = "Words to Find:",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color(0xFF004D40)  // Dark green
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                displayedWords.forEachIndexed { index, word ->
                    val displayWord = if (index == wordsToFind.lastIndex && foundWords.contains(hiddenWord)) {
                        hiddenWord
                    } else {
                        word
                    }
                    Text(
                        text = displayWord,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 24.sp,
                            textDecoration = if (foundWords.contains(displayWord)) TextDecoration.LineThrough else null,
                            color = if (foundWords.contains(displayWord)) Color(0xFF9E9E9E) else Color(0xFF212121)  // Gray out found words
                        )
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
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = cell.letter.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF004D40)  // Dark green text for letters
            )
        )
    }
}
