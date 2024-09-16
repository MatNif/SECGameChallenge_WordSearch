package com.example.secgamecallenge_wordsearch

import android.content.Context
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.*

// Function to store player information in the app's local storage
fun storePlayerInfo(context: Context, player: Player) {
    // Check if the top players list needs to be reset (done daily)
    resetTopPlayersIfNeeded(context)

    // Access the shared preferences for storing data
    val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    val editor = prefs.edit()

    // Retrieve the current list of top players and add the new player
    val players = getTopPlayers(context).toMutableList()
    players.add(player)
    // Sort players by time (fastest first) and then by the number of words found (highest first)
    players.sortBy { it.time }
    players.sortByDescending { it.wordsFound }


    // Keep only the top 10 players
    val topPlayers = players.take(10)
    // Save the updated list of top players back to shared preferences
    editor.putString("top_players", Gson().toJson(topPlayers))
    editor.apply()
}

// Function to reset the top players list if a new day has started
fun resetTopPlayersIfNeeded(context: Context) {
    // Access the shared preferences for storing data
    val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    // Get the date of the last reset
    val lastResetDate = prefs.getString("last_reset_date", "")
    // Get the current date in yyyyMMdd format
    val currentDate = getCurrentDateString()

    // If the last reset date is not the same as the current date, reset the top players list
    if (lastResetDate != currentDate) {
        val editor = prefs.edit()
        // Clear the top players list
        editor.putString("top_players", Gson().toJson(emptyList<Player>()))
        // Update the last reset date to the current date
        editor.putString("last_reset_date", currentDate)
        editor.apply()
    }
}

// Helper function to get the current date as a string in yyyyMMdd format
fun getCurrentDateString(): String {
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return dateFormat.format(Date())
}

// Function to retrieve the list of top players from the app's local storage
fun getTopPlayers(context: Context): List<Player> {
    // Access the shared preferences for storing data
    val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    // Get the JSON string of top players
    val json = prefs.getString("top_players", "[]")
    // Convert the JSON string back to a list of Player objects
    return Gson().fromJson(json, Array<Player>::class.java).toList()
}
