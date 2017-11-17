package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 17/11/17.
 */

import android.app.Application
import android.content.Context
import android.util.Log

import ai.api.util.BluetoothController

class AIApplication : Application() {

    private var activitiesCount: Int = 0
    lateinit var bluetoothController: BluetoothControllerImpl
        private set
    //'private set' designates that the setter is private
    //so the getter remains public, and the object is read-only
    //as private setter allows modification of object only inside the class
    lateinit var settingsManager: SettingsManager
        private set

    private val isInForeground: Boolean
        get() = activitiesCount > 0

    override fun onCreate() {
        super.onCreate()
        bluetoothController = BluetoothControllerImpl(this)
        settingsManager = SettingsManager(this)
    }

    fun getBluetoothController(): BluetoothController {
        return bluetoothController
    }

    fun onActivityResume() {
        if (activitiesCount++ == 0) { // on become foreground
            if (settingsManager.isUseBluetooth) {
                bluetoothController.start()
            }
        }
    }

    fun onActivityPaused() {
        if (--activitiesCount == 0) { // on become background
            bluetoothController.stop()
        }
    }

    inner class BluetoothControllerImpl(context: Context) : BluetoothController(context) {

        override fun onHeadsetDisconnected() {
            Log.d(TAG, "Bluetooth headset disconnected")
        }

        override fun onHeadsetConnected() {
            Log.d(TAG, "Bluetooth headset connected")

            if (isInForeground && settingsManager.isUseBluetooth
                    && !bluetoothController.isOnHeadsetSco) {
                bluetoothController.start()
            }
        }

        override fun onScoAudioDisconnected() {
            Log.d(TAG, "Bluetooth sco audio finished")
            bluetoothController.stop()

            if (isInForeground && settingsManager.isUseBluetooth) {
                bluetoothController.start()
            }
        }

        override fun onScoAudioConnected() {
            Log.d(TAG, "Bluetooth sco audio started")
        }

    }

    companion object {

        private val TAG = AIApplication::class.java.name
    }

}