package com.example.secgamecallenge_wordsearch

import android.content.Context
import com.google.gson.Gson

fun storePlayerTime(context: Context, player: Player) {
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

fun getTopPlayers(context: Context): List<Player> {
    val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    val json = prefs.getString("top_players", "[]")
    return Gson().fromJson(json, Array<Player>::class.java).toList()
}
