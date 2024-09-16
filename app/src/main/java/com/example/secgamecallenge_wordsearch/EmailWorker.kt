package com.example.secgamecallenge_wordsearch

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

val listOfPrizes = listOf("a gift card", "a t-shirt", "a mug", "a keychain")

class EmailWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val topPlayers = getTopPlayers(applicationContext)
        sendEmailsToTopPlayers(topPlayers)
        return Result.success()
    }

    private fun sendEmailsToTopPlayers(players: List<Player>) {
        players.forEach { player ->
            // Award a random prize
            val prize = listOfPrizes.random()
            // Generate the email body
            val emailBody = "Congratulations ${player.name}!\n" +
                    "You were one of the top 10 players in the SEC game challenge today!\n" +
                    "You found ${player.wordsFound} words in ${player.time} seconds.\n" +
                    "You have won $prize!"
            // Use your email API here to send the email
            sendEmail(player.email, "Congratulations ${player.name}!", emailBody)
        }
    }
}