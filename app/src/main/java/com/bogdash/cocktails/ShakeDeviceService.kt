package com.bogdash.cocktails

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

/**
 * ShakeDeviceService lets you perform actions on detected device shakes.
 *
 * @param context activity's context
 * @param action action to perform when device's been shaken
 */
class ShakeDeviceService(
    context: Context,
    val action: () -> Unit
) : SensorEventListener {

    private var lastShakeTime: Long = 0
    private val shakeThreshold = 1000
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastShakeTime > shakeThreshold) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val acceleration = sqrt((x * x + y * y + z * z))
                if (acceleration > 50) {
                    action()
                    lastShakeTime = currentTime
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun subscribe() {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unsubscribe() {
        sensorManager.unregisterListener(this)
    }

}
