package com.example.app_1a.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.app_1a.R

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "level_up_channel"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Level-Up Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones para la app Level-Up Gamer"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showWelcomeNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Bienvenido a Level-Up Gamer")
            .setContentText("Tu aventura está por comenzar. ¡Explora nuestros productos!")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de tener este drawable
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notificationManager.notify(1, notification)
    }

    fun showEventNotification(eventName: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Recordatorio de Evento")
            .setContentText("Se te notificará cuando sea el día de este evento: $eventName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // ARREGLO: Pasamos el ID único (hashCode) y el objeto notification.
        notificationManager.notify(eventName.hashCode(), notification)
    }
}

