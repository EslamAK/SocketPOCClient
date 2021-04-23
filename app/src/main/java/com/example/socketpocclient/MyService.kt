package com.example.socketpocclient

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import io.socket.client.Socket

class MyService : Service() {
    private var socket: Socket? = null
    private var id: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        socket = (application as MyApplication).socket

        val notification = createNotification()
        startForeground(1, notification)

        socket?.on(Socket.EVENT_CONNECT) {
            id = socket?.id()
            Log.d("Eslam", "Socket with id: $id is connected ✅")
        }

        socket?.on(Socket.EVENT_DISCONNECT) {
            Log.d("Eslam", "Socket with id: $id is disconnected 🚫")
        }

        socket?.connect()

        return START_STICKY

    }

    private fun createNotification(): Notification? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(
                    NotificationChannel(
                        "channelID", "channelID", NotificationManager.IMPORTANCE_DEFAULT
                    )
                )
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, "channelID")
            .setContentTitle("notificationTitle")
            .setContentText("contentText")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
        return notification
    }

    override fun onDestroy() {
        super.onDestroy()
        socket?.disconnect()
        Log.d("Eslam", "Socket with id: $id is disconnected 🚫")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
