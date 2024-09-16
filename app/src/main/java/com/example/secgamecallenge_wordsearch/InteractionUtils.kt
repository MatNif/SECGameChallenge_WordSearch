package com.example.secgamecallenge_wordsearch

import androidx.compose.ui.geometry.Offset

// Function to check if a given position is within the bounds of a grid cell
fun isInCellBounds(cell: GridCell, position: Offset): Boolean {
    return position.x >= cell.topLeft.x && position.x <= cell.bottomRight.x &&
            position.y >= cell.topLeft.y && position.y <= cell.bottomRight.y
}

// Function to check if a list of selected cells forms a straight line (horizontal, vertical, or diagonal)
fun isSelectionStraight(cells: List<GridCell>): Boolean {
    if (cells.size < 2) return true

    val firstCell = cells.first()
    var isHorizontal = false
    var isVertical = false
    var isDiagonal = false

    // Check if the selection is horizontal
    for (i in 1 until cells.size) {
        val cell = cells[cells.size - i]
        if (cell.row == firstCell.row) {
            isHorizontal = true
        } else {
            isHorizontal = false
            break
        }
    }

    // Check if the selection is vertical
    for (i in 1 until cells.size) {
        val cell = cells[cells.size - i]
        if (cell.col == firstCell.col) {
            isVertical = true
        } else {
            isVertical = false
            break
        }
    }

    // Check if the selection is diagonal
    for (i in 1 until cells.size) {
        val cell = cells[cells.size - i]
        if (cell.row - firstCell.row == cell.col - firstCell.col ||
            cell.row - firstCell.row == firstCell.col - cell.col) {
            isDiagonal = true
        } else {
            isDiagonal = false
            break
        }
    }

    // Return true if the selection is either horizontal, vertical, or diagonal
    return isHorizontal || isVertical || isDiagonal
}

// Function to adjust a list of selected cells to form a straight line if possible
fun straightenSelection(cells: List<GridCell>, grid: List<List<GridCell>>): List<GridCell> {

    val firstCell = cells.first()
    val lastCell = cells.last()
    var lastCellInLine = GridCell()

    // Find the last cell in the line that forms a straight line
    for (i in 1 until cells.size) {
        val consecutiveCells = cells.subList(0, cells.size - i)
        if (isSelectionStraight(consecutiveCells)) {
            lastCellInLine = cells[cells.size - i]
            break
        }
    }

    // Redraw a straight line if the last cell is in line with the first cell
    if (manhattanDistance(lastCellInLine, lastCell) < 2 && isSelectionStraight(listOf(firstCell, lastCell))) {
        return drawStraightLine(firstCell, lastCell, grid)
    } else {
        // Remove the non-straight part of the line
        return cells.subList(0, cells.indexOf(lastCellInLine))
    }
}

// Function to calculate the Manhattan distance between two cells
fun manhattanDistance(cell1: GridCell, cell2: GridCell): Int {
    return Math.abs(cell1.row - cell2.row) + Math.abs(cell1.col - cell2.col)
}

// Function to draw a straight line of cells between two given cells
fun drawStraightLine(start: GridCell, end: GridCell, grid: List<List<GridCell>>): List<GridCell> {
    val cells = mutableListOf<GridCell>()

    // Draw a horizontal line if the start and end cells are in the same row
    if (start.row == end.row) {
        val row = start.row
        val startCol = Math.min(start.col, end.col)
        val endCol = Math.max(start.col, end.col)
        for (col in startCol..endCol) {
            cells.add(grid[row][col])
        }

        // Draw a vertical line if the start and end cells are in the same column
    } else if (start.col == end.col) {
        val col = start.col
        val startRow = Math.min(start.row, end.row)
        val endRow = Math.max(start.row, end.row)
        for (row in startRow..endRow) {
            cells.add(grid[row][col])
        }

        // Draw a diagonal line if the start and end cells are diagonally aligned
    } else if (Math.abs(start.row - end.row) == Math.abs(start.col - end.col)) {
        if (start.row < end.row && start.col < end.col) {
            for (i in 0..Math.abs(start.row - end.row)) {
                cells.add(grid[start.row + i][start.col + i])
            }
        } else if (start.row < end.row && start.col > end.col) {
            for (i in 0..Math.abs(start.row - end.row)) {
                cells.add(grid[start.row + i][start.col - i])
            }
        } else if (start.row > end.row && start.col < end.col) {
            for (i in 0..Math.abs(start.row - end.row)) {
                cells.add(grid[start.row - i][start.col + i])
            }
        } else if (start.row > end.row && start.col > end.col) {
            for (i in 0..Math.abs(start.row - end.row)) {
                cells.add(grid[start.row - i][start.col - i])
            }
        }

        // If the start and end cells are not in a straight line, return the start cell
    } else {
        cells.add(start)
    }
    return cells
}