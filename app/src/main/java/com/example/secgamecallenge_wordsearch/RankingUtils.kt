package com.example.secgamecallenge_wordsearch

import android.content.Context
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.*

fun storePlayerInfo(context: Context, player: Player) {
    // Check if the top players list needs to be reset
    resetTopPlayersIfNeeded(context)  // Reset every day

    val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    val editor = prefs.edit()

    // Retrieve and update the list of players
    val players = getTopPlayers(context).toMutableList()
    players.add(player)
    players.sortBy { it.time }  // Sort by time (fastest first)

    // Store top 10 players
    val topPlayers = players.take(10)
    editor.putString("top_players", Gson().toJson(topPlayers))
    editor.apply()
}

// Reset the top players list if a new day has begun since the last reset
fun resetTopPlayersIfNeeded(context: Context) {
    val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    val lastResetDate = prefs.getString("last_reset_date", "")  // Get the last reset date as a string
    val currentDate = getCurrentDateString()  // Get the current date as yyyyMMdd

    if (lastResetDate != currentDate) {
        // Reset top players and update the reset date
        val editor = prefs.edit()
        editor.putString("top_players", Gson().toJson(emptyList<Player>()))  // Clear top players list
        editor.putString("last_reset_date", currentDate)  // Update last reset date
        editor.apply()
    }
}

// Helper function to get the current date as yyyyMMdd
fun getCurrentDateString(): String {
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return dateFormat.format(Date())
}

fun getTopPlayers(context: Context): List<Player> {
    val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    val json = prefs.getString("top_players", "[]")
    return Gson().fromJson(json, Array<Player>::class.java).toList()
}
