package com.example.secgamecallenge_wordsearch

import androidx.compose.ui.geometry.Offset

fun isInCellBounds(cell: GridCell, position: Offset): Boolean {
    return position.x >= cell.topLeft.x && position.x <= cell.bottomRight.x &&
            position.y >= cell.topLeft.y && position.y <= cell.bottomRight.y
}

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

    return isHorizontal || isVertical || isDiagonal
}


fun straightenSelection(cells: List<GridCell>, grid: List<List<GridCell>>): List<GridCell> {

    val firstCell = cells.first()
    val lastCell = cells.last()
    var lastCellInLine = GridCell()

    // Find the last cell in the line
    for (i in 1 until cells.size) {
        val consecutiveCells = cells.subList(0, cells.size - i)
        if (isSelectionStraight(consecutiveCells)) {
            lastCellInLine = cells[cells.size - i]
            break
        }
    }

    // Redraw a straight line if:
    // 1. the manhattan distance between the last cell and the last cell in the line is >2
    // 2. and the last cell is in line horizontally, vertically or diagonally with the first cell
    if (manhattanDistance(lastCellInLine, lastCell)<2 && isSelectionStraight(listOf(firstCell, lastCell))) {
        return drawStraightLine(firstCell, lastCell, grid)
    } else {
        // else remove the non-straight part of the line
        return cells.subList(0, cells.indexOf(lastCellInLine))
    }


}

fun manhattanDistance(cell1: GridCell, cell2: GridCell): Int {
    return Math.abs(cell1.row - cell2.row) + Math.abs(cell1.col - cell2.col)
}

fun drawStraightLine(start: GridCell, end: GridCell, grid: List<List<GridCell>>): List<GridCell> {
    val cells = mutableListOf<GridCell>()

    // If the start and end cell.row are the same, draw a horizontal line
    if (start.row == end.row) {
        val row = start.row
        val startCol = Math.min(start.col, end.col)
        val endCol = Math.max(start.col, end.col)
        for (col in startCol..endCol) {
            cells.add(grid[row][col])
        }

    // If the start and end cell.col are the same, draw a vertical line
    } else if (start.col == end.col) {
        val col = start.col
        val startRow = Math.min(start.row, end.row)
        val endRow = Math.max(start.row, end.row)
        for (row in startRow..endRow) {
            cells.add(grid[row][col])
        }

    // If the start and end cells are diagonally offset, draw a diagonal line
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