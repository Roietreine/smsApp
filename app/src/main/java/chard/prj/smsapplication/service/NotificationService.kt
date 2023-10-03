package chard.prj.smsapplication.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import chard.prj.smsapplication.R
import chard.prj.smsapplication.ui.MainActivity
import chard.prj.smsapplication.utils.Constant.CHANNEL_ID

class NotificationService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        // Check if the notification is an SMS notification
        if (isSmsNotification(sbn)) {
            // Extract relevant notification information
            val notification = sbn.notification
            val title = notification.extras.getString(Notification.EXTRA_TITLE)
            val text = notification.extras.getString(Notification.EXTRA_TEXT)

            // Create a pending intent to launch your app's main activity when the notification is clicked
            val permissionIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                permissionIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            // Create a notification manager and a notification channel
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

            // Build the notification
            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)

            // Show the notification
            val result = notificationManager.notify(sbn.id, notificationBuilder.build())
            Log.d("MyNotificationListener", "Notification result: $result")
        }
    }

    private fun isSmsNotification(sbn: StatusBarNotification): Boolean {
        val notification = sbn.notification
        val title = notification.extras?.getString(Notification.EXTRA_TITLE)
        val text = notification.extras?.getString(Notification.EXTRA_TEXT)

        // Check if the notification content contains typical SMS keywords
        val smsKeywords = listOf("SMS", "Messages", "Text","Message")

        return title?.containsAny(smsKeywords) == true || text?.containsAny(smsKeywords) == true
    }

    // Extension function to check if a string contains any of the specified keywords
    private fun String?.containsAny(keywords: List<String>): Boolean {
        return this?.let { content ->
            keywords.any { keyword -> content.contains(keyword, ignoreCase = true) }
        } ?: false
    }
}