package com.example.lecture15_kotlin

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sensor.*

class SensorActivity : AppCompatActivity() {
    private lateinit var mSensorManager: SensorManager
    private lateinit var mSensorEventListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                val sensor = event!!.sensor

                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    display_acceleration.text = "X:  ${event.values[0]}  Y:  ${event.values[1]}   Z:  ${event.values[2]}"

                    if (event.values[0] > event.values[1] && event.values[0] > event.values[2]) {
                        display_greater_value!!.text = event.values[0].toString()
                    } else if (event.values[1] > event.values[0] && event.values[1] > event.values[2]) {
                        display_greater_value!!.text = event.values[1].toString()
                    } else {
                        display_greater_value!!.text = event.values[2].toString()
                    }
                } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    display_proximity!!.text = "The Value  ${event.values[0]}"
                }

                mSensorManager.registerListener(
                    mSensorEventListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL
                )
                mSensorManager.registerListener(
                    mSensorEventListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                    SensorManager.SENSOR_DELAY_NORMAL
                )

            }

        }
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(mSensorEventListener)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(
            mSensorEventListener,
            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        mSensorManager.registerListener(
            mSensorEventListener,
            mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun onNextBtnClicked(view : View){
        val intent = Intent(this, BroadcastMessage :: class.java)
        startActivity(intent)
    }
}
