package com.example.secgamecallenge_wordsearch

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import android.content.Context

const val hostServer = "smtp.office365.com"
const val hostPort = "587"
const val gameMasterEmail = "your_email@example.com"
const val gameMasterPassword = "your_password"


fun scheduleEmailTask(applicationContext: Context) {
    val now = Calendar.getInstance()
    val midnight = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    if (now.after(midnight)) {
        midnight.add(Calendar.DAY_OF_MONTH, 1)  // Schedule for the next midnight
    }

    val delay = midnight.timeInMillis - now.timeInMillis

    val emailRequest = OneTimeWorkRequestBuilder<EmailWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(applicationContext).enqueue(emailRequest)
}


fun sendEmail(toEmail: String, subject: String, body: String) {
    val session = createEmailSession()
    val message = MimeMessage(session).apply {
        setFrom(InternetAddress(gameMasterEmail))
        setRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
        this.subject = subject
        setText(body)
    }

    Transport.send(message)
}

fun createEmailSession(): Session {
    val props = Properties().apply {
        put("mail.smtp.host", hostServer)
        put("mail.smtp.port", hostPort)
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
    }

    return Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(gameMasterEmail, gameMasterPassword)
        }
    })
}
