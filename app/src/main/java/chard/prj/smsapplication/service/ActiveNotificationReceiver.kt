package chard.prj.smsapplication.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import chard.prj.smsapplication.R
import chard.prj.smsapplication.ui.MainActivity
import chard.prj.smsapplication.utils.Constant
import chard.prj.smsapplication.utils.Constant.CHANNEL_ID
import chard.prj.smsapplication.utils.Constant.GROUP_KEY
import chard.prj.smsapplication.utils.Constant.NOTIFICATION_ID

class ActiveNotificationReceiver : NotificationListenerService() {
    val context = applicationContext

    override fun onNotificationPosted(notification: StatusBarNotification) {
        // Create a pending intent to launch your app's main activity when the notification is clicked
        val permissionIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            permissionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create a notification manager and a notification channel
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Active Notification Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        // Get the active notifications
        val activeNotifications = notificationManager.activeNotifications

        // Create a list of notification summaries
        val notificationSummaryList = ArrayList<String>()
        for (notification in activeNotifications) {
            val notificationTitle = notification.notification.extras.getString(Notification.EXTRA_TITLE)
            if (notificationTitle != null) {
                notificationSummaryList.add(notificationTitle)
            }
        }

        // Create a group notification that summarizes the active notifications
        val groupNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message)
            .setGroupSummary(true)
            .setGroup(GROUP_KEY)
            .setContentTitle("Active Notifications")
            .setContentText("${activeNotifications.size} notifications")
            .setContentIntent(pendingIntent)
            .build()

        // Add individual notifications to the group
        for (notification in activeNotifications) {
            val notificationTitle = notification.notification.extras.getString(Notification.EXTRA_TITLE)
            val notificationText = notification.notification.extras.getString(Notification.EXTRA_TEXT)
            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setGroup(GROUP_KEY)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(notification.id, notificationBuilder)
        }

        // Notify the group notification
        notificationManager.notify(Constant.GROUP_ID, groupNotification)
    }
}