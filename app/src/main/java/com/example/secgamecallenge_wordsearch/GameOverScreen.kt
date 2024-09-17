package com.example.secgamecallenge_wordsearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color

@Composable
fun GameOverScreen(isWinner: Boolean, currentPlayer: Player, topPlayers: List<Player>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Game over text, green for winners, red for time's up
        Text(
            text = if (isWinner) "You Won!" else "Time's Up!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (isWinner) Color(0xFF00796B) else Color(0xFFD32F2F),  // Green for win, red for lose
                fontSize = 32.sp
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Show player's time and number of words found
        Text(
            text = "Number Of Words Found: ${currentPlayer.wordsFound}       " +
                    "In A Time Of: ${currentPlayer.time / 60}:${(currentPlayer.time % 60).toString().padStart(2, '0')}",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 24.sp,
                color = Color(0xFF004D40)  // Dark green
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Display top 10 fastest players
        Text(
            text = "Top 10 Fastest Players",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF004D40),  // Dark green
                fontSize = 28.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        // List top players
        // Table layout with constrained width
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)  // Limit the width of the table to half the screen size
                .padding(horizontal = 16.dp)
        ) {
            // Table headers with increased font size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rank",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp  // Increase font size for headers
                    ),
                    modifier = Modifier.weight(1.2f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp  // Increase font size for headers
                    ),
                    modifier = Modifier.weight(4f),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Time",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp  // Increase font size for headers
                    ),
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Words",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp  // Increase font size for headers
                    ),
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // List top players as rows in the table
            topPlayers.forEachIndexed { index, player ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = if (player.name == currentPlayer.name) FontWeight.Bold else FontWeight.Normal
                        ),
                        modifier = Modifier.weight(1.2f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 22.sp,
                            color = if (player.name == currentPlayer.name) Color(0xFF00796B) else Color.Black,  // Highlight the fastest player with green
                            fontWeight = if (player.name == currentPlayer.name) FontWeight.Bold else FontWeight.Normal
                        ),
                        modifier = Modifier.weight(4f),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "${player.time / 60}:${(player.time % 60).toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = if (player.name == currentPlayer.name) FontWeight.Bold else FontWeight.Normal
                        ),
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Center  // Center-align time values
                    )
                    Text(
                        text = "${player.wordsFound}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = if (player.name == currentPlayer.name) FontWeight.Bold else FontWeight.Normal
                        ),
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Center  // Center-align words found
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))  // Space between rows
            }
        }
    }
}
