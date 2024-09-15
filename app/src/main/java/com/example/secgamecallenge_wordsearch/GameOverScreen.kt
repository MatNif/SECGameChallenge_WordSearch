package com.example.secgamecallenge_wordsearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameOverScreen(isWinner: Boolean, playerTime: Int, topPlayers: List<Player>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isWinner) "You Won!" else "Time's Up!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Show player's time
        Text(
            text = "Your Time: ${playerTime / 60}:${(playerTime % 60).toString().padStart(2, '0')}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Display top 10 fastest players
        Text(text = "Top 10 Fastest Players", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Column {
            topPlayers.forEachIndexed { index, player ->
                Text(
                    text = "${index + 1}. ${player.name}: ${player.time / 60}:${(player.time % 60).toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
