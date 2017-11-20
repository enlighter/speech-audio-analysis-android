package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 17/11/17.
 */

import android.content.Context
import android.content.SharedPreferences

import ai.api.util.BluetoothController

class SettingsManager(private val context: Context) {
    private val prefs: SharedPreferences

    private var useBluetooth: Boolean = false

    var isUseBluetooth: Boolean
        get() = useBluetooth
        set(useBluetooth) {
            this.useBluetooth = useBluetooth

            prefs.edit().putBoolean(PREF_USE_BLUETOOTH, useBluetooth).apply()
            val controller = (context.applicationContext as AIApplication).getBluetoothController()
            if (useBluetooth) {
                controller.start()
            } else {
                controller.stop()
            }
        }

    init {
        prefs = context.getSharedPreferences(SETTINGS_PREFS_NAME, Context.MODE_PRIVATE)

        useBluetooth = prefs.getBoolean(PREF_USE_BLUETOOTH, true)
    }

    companion object {

        private val SETTINGS_PREFS_NAME = "ai.api.APP_SETTINGS"
        private val PREF_USE_BLUETOOTH = "USE_BLUETOOTH"
    }

}