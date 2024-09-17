package com.example.secgamecallenge_wordsearch

// Function to evaluate a random chance of X% (returns true or false)
fun randomChance(chance: Double): Boolean {
    return Math.random() < chance
}

// Function to calculate the Manhattan distance between two cells
fun manhattanDistance(cell1: GridCell, cell2: GridCell): Int {
    return Math.abs(cell1.row - cell2.row) + Math.abs(cell1.col - cell2.col)
}