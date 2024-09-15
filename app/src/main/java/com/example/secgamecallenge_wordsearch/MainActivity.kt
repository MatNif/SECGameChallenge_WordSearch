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

@OptIn(ExperimentalMaterial3Api::class)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SECGameCallenge_WordSearchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainGameScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordSearchGamePreview() {
    SECGameCallenge_WordSearchTheme {
        MainGameScreen()
    }
}

@Composable
fun MainGameScreen(modifier: Modifier = Modifier) {
    var gameOver by remember { mutableStateOf(false) }
    var isWinner by remember { mutableStateOf(false) }
    var wordsFound by remember { mutableStateOf(0) }
    var playerTime by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val topPlayers = getTopPlayers(context)

    if (gameOver) {
        GameOverScreen(isWinner, playerTime, topPlayers)
    } else {
        WordSearchGame(modifier) { won, time, words ->
            isWinner = won
            playerTime = time
            wordsFound = words
            gameOver = true
            storePlayerTime(context, Player("PlayerName", words, time)) // Example: use actual player name
        }
    }
}
