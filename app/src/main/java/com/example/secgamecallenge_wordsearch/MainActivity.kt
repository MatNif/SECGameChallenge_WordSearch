package com.example.secgamecallenge_wordsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.secgamecallenge_wordsearch.ui.theme.SECGameCallenge_WordSearchTheme
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Main activity of the app
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the Composable function
        setContent {
            SECGameCallenge_WordSearchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainGameScreen(modifier = Modifier.padding(innerPadding), player = testPlayer)
                }
            }
        }

        // Schedule the email task at midnight every day on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            scheduleEmailTask(applicationContext)
        }
    }
}

// Preview the game screen
@Preview(showBackground = true)
@Composable
fun WordSearchGamePreview() {
    SECGameCallenge_WordSearchTheme {
        val player = Player("PreviewPlayer", "employee@sec.ethz.ch", 0, 0)
        MainGameScreen(player=player)
    }
}

// Main game screen that contains the game logic
@Composable
fun MainGameScreen(modifier: Modifier = Modifier, player: Player) {
    // State variables to manage game status
    var gameOver by remember { mutableStateOf(false) }
    var isWinner by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val topPlayers = getTopPlayers(context)

    // Display the appropriate screen based on game status
    if (gameOver) {
        GameOverScreen(isWinner, player, topPlayers)
    } else {
        WordSearchGame(modifier) { won, time, words ->
            // Update game state when the game is over
            gameOver = true
            isWinner = won
            player.time = time
            player.wordsFound = words
            storePlayerInfo(context, player) // Store player information
        }
    }
}