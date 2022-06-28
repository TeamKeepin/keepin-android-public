package com.keepin.android.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.keepin.android.R
import com.keepin.android.presentation.main.MainActivity

class KeepInMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            noticeReminder(remoteMessage)
            TimberUtil.timber("fcm_message", remoteMessage.data["title"].toString())
            TimberUtil.timber("fcm_message", remoteMessage.data["body"].toString())
            TimberUtil.timber("fcm_message", remoteMessage.data.toString())
        }
    }

    private fun noticeReminder(remoteMessage: RemoteMessage) {
        val alarmId = remoteMessage.sentTime.toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("openFromFcm", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, FLOAT_REMINDER, intent, PendingIntent.FLAG_ONE_SHOT)
        val builder =
            NotificationCompat.Builder(this, getString(R.string.app_name))
                .setContentTitle(remoteMessage.data["title"].toString())
                .setContentText(remoteMessage.data["body"].toString())
                .setSmallIcon(R.drawable.ic_push_alarm_logo)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.app_name)
            val channelName = getString(R.string.app_name)
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, channelImportance)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(alarmId, builder.build())
    }

    companion object {
        const val FLOAT_REMINDER = 0
    }
}
