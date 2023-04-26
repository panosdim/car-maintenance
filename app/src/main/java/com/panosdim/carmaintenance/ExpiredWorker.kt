package com.panosdim.carmaintenance

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate.now

class ExpiredWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        val mBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Car Needs Maintenance")
            .setContentText("Some car needs maintenance.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        user?.let { database.getReference("cars").child(it.uid) }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    // Not used
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val today = now()
                    dataSnapshot.children.forEach { car ->
                        val item = car.getValue(Car::class.java)

                        if (item != null &&
                            (item.kteo.date.toLocalDate().minusWeeks(1).isBefore(today)
                                    || item.kteo.exhaustCard.toLocalDate().minusWeeks(1)
                                .isBefore(today))
                        ) {
                            with(NotificationManagerCompat.from(applicationContext)) {
                                if (ActivityCompat.checkSelfPermission(
                                        applicationContext,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    notify(0, mBuilder.build())
                                }
                            }
                        }
                    }
                }
            })

        // Indicate success or failure with your return value:
        return Result.success()
    }

}
