package com.example.secgamecallenge_wordsearch

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

// Worker class to handle sending emails to top players
class EmailWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    // Main function that gets executed when the worker runs
    override fun doWork(): Result {
        // Retrieve the list of top players
        val topPlayers = getTopPlayers(applicationContext)
        // Send emails to the top players
        sendEmailsToTopPlayers(topPlayers)
        // Indicate that the work finished successfully
        return Result.success()
    }

    // Function to send emails to a list of players
    private fun sendEmailsToTopPlayers(players: List<Player>) {
        players.forEach { player ->
            // Select a random prize from the list
            val prize = listOfPrizes.random()
            // Create the email content
            val emailBody = "Congratulations ${player.name}!\n" +
                    "You were one of the top 10 players in the SEC game challenge today!\n" +
                    "You found ${player.wordsFound} words in ${player.time} seconds.\n" +
                    "You have won $prize!"
            // Send the email using an email API
            sendEmail(player.email, "Congratulations ${player.name}!", emailBody)
        }
    }
}