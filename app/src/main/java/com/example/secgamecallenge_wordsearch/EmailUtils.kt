package com.example.secgamecallenge_wordsearch

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import android.content.Context

// Function to schedule an email task to run at midnight
fun scheduleEmailTask(applicationContext: Context) {
    val now = Calendar.getInstance() // Get the current time
    val midnight = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0) // Set the time to midnight
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // If the current time is after midnight, schedule for the next midnight
    if (now.after(midnight)) {
        midnight.add(Calendar.DAY_OF_MONTH, 1)
    }

    val delay = midnight.timeInMillis - now.timeInMillis // Calculate the delay until midnight

    // Create a one-time work request to send emails at the calculated delay
    val emailRequest = OneTimeWorkRequestBuilder<EmailWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    // Enqueue the work request with WorkManager
    WorkManager.getInstance(applicationContext).enqueue(emailRequest)
}

// Function to send an email
fun sendEmail(toEmail: String, subject: String, body: String) {
    val session = createEmailSession() // Create an email session
    val message = MimeMessage(session).apply {
        setFrom(InternetAddress(gameMasterEmail)) // Set the sender's email address
        setRecipient(Message.RecipientType.TO, InternetAddress(toEmail)) // Set the recipient's email address
        this.subject = subject // Set the email subject
        setText(body) // Set the email body
    }

    Transport.send(message) // Send the email
}

// Function to create an email session with the server
fun createEmailSession(): Session {
    val props = Properties().apply {
        put("mail.smtp.host", hostServer) // Set the SMTP server host
        put("mail.smtp.port", hostPort) // Set the SMTP server port
        put("mail.smtp.auth", "true") // Enable authentication
        put("mail.smtp.starttls.enable", "true") // Enable STARTTLS
    }

    // Return a session with the specified properties and authentication
    return Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(gameMasterEmail, gameMasterPassword) // Provide the email and password for authentication
        }
    })
}