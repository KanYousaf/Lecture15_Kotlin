package com.example.lecture15_kotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Notification
import android.content.Context
import android.graphics.Color


class MainActivity : AppCompatActivity() {
    private lateinit var notification_builder : Notification.Builder
    private lateinit var sensor_intent : Intent
    private val color = Color.RED
    private val light_color = Color.argb(255, 255, 255, 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensor_intent = Intent(this, SensorActivity::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            notification_builder =Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
                .setColor(color)
                .setContentTitle("Welcome to Notification")
                .setContentText("Let's go to Sensor Act")
                .setLights(light_color, 2000, 3000)
            notificationManager()
        }else {

            notification_builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
                .setContentTitle("Welcome to Notification")
                .setContentText("Let's go to Sensor Act")
                .setLights(light_color, 2000, 3000)
            notificationManager()
        }
    }

    fun notificationManager(){
        val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, sensor_intent, 0)
        notification_builder.setContentIntent(pendingIntent)
        val notification = notification_builder.build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(99, notification)

    }
}
