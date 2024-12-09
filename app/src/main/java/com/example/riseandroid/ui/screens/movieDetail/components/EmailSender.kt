package com.example.riseandroid.ui.screens.movieDetail.components

import android.content.Context
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.mail.*
import javax.mail.internet.*

class EmailSender(
    private val username: String,
    private val password: String
) {
    suspend fun sendEmail(to: String, subject: String, body: String) {
        withContext(Dispatchers.IO) {
            val properties = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.host", "smtp.gmail.com")
                put("mail.smtp.port", "587")
            }

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            })

            try {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(username))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                    setSubject(subject)
                    setContent(body, "text/html; charset=utf-8")
                }
                Transport.send(message)
            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }
}

