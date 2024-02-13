package com.teamtriad.forpets.data.source.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teamtriad.forpets.R
import com.teamtriad.forpets.ui.MainActivity

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body

            if (checkNotificationPermission()) {
                showNotification(title, body)
            }
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "My_Channel_ID"
        val notificationId = 123

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigateToFragment", "ChatRoomFragment")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_adopt)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel Title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun checkNotificationPermission(): Boolean {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val packageName: String = packageName
            intent.data = Settings.ACTION_APPLICATION_DETAILS_SETTINGS.toUri()
                .buildUpon()
                .appendPath(packageName)
                .build()

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )

            val notificationBuilder = NotificationCompat.Builder(this, "My_Channel_ID")
                .setSmallIcon(R.drawable.ic_adopt)
                .setContentTitle("알림 권한 확인")
                .setContentText("알림 권한이 필요합니다. 터치하여 설정으로 이동하세요.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "My_Channel_ID",
                    "Channel Title",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(123, notificationBuilder.build())

            return false
        }
        return true
    }

}
