package com.devstree.annibee.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.activity.AnniversaryDetailActivity
import com.devstree.annibee.activity.EventDetailActivity
import com.devstree.annibee.activity.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "onMessageReceived")
        if (remoteMessage.data.isNullOrEmpty()) return
        Log.e(TAG, "Notification Data " + Gson().toJson(remoteMessage.data))

        val param: Map<String, String> = remoteMessage.data

        val title = param["title"]
        val body = param["body"]
        val type = param["type"]
        val id = param["id"]
        val userId = param["user_id"]
        val badge = param["badge"]
        val notificationId = System.currentTimeMillis().toInt()


        if (id != null && type?.isNotEmpty() == true) {
            show(
                notificationId,
                title,
                body,
                id.toLong(),
                type,
                pendingIntent(Controller.instance, id.toLong(), type, notificationId)
            )
        }
    }


    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_MESSAGE_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(Controller.instance).createNotificationChannel(channel)
        }
    }

    fun show(
        notification_id: Int,
        title: String?,
        contentText: String?,
        id: Long,
        type: String?,
        pendingIntent: PendingIntent?
    ) {
        createChannel()
        val context: Context = Controller.instance
        val builder = NotificationCompat.Builder(context, CHANNEL_MESSAGE_ID)
            .setStyle(
                NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(contentText)
            )
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)

        if (pendingIntent != null && type?.isNotEmpty() == true) builder.setContentIntent(
            pendingIntent(
                context,
                id,
                type,
                notification_id
            )
        )
        NotificationManagerCompat.from(context).notify(notification_id, builder.build())
    }

    private fun pendingIntent(
        context: Context,
        id: Long,
        type: String,
        unique_id: Int
    ): PendingIntent {
        when (type) {
            "1" -> {
                val intent = Intent(context, AnniversaryDetailActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("anniversary_id", id)
                return PendingIntent.getActivity(
                    context,
                    unique_id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            "2" -> {
                val intent = Intent(context, EventDetailActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("event_id", id)
                return PendingIntent.getActivity(
                    context,
                    unique_id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            else -> {
                val intent = Intent(context, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("position", 0)
                return PendingIntent.getActivity(
                    context,
                    unique_id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        }
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("fcmToken", token)
    }

    companion object {
        private val TAG = FirebaseNotificationService::class.java.simpleName

        val TYPE_GENERAL = "general_notification"


        private const val CHANNEL_MESSAGE_ID = "notification_channel_id"
        private const val CHANNEL_NAME = "Notification"

    }
}