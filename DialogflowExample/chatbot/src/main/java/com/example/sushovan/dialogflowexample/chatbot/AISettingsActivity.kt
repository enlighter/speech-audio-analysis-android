package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 20/11/17.
 */

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton

/***********************************************************************************************************************
 * API.AI Android SDK -  API.AI libraries usage example
 * =================================================
 *
 *
 * Copyright (C) 2015 by Speaktoit, Inc. (https://www.speaktoit.com)
 * https://www.api.ai
 *
 *
 * **********************************************************************************************************************
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

class AISettingsActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var bluetoothSwitch: CheckBox

    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsManager = (application as AIApplication).settingsManager

        val bluetoothSection = findViewById<View>(
                R.id.activity_settings_bluetooth_section) as ViewGroup
        bluetoothSection.setOnClickListener(this)

        bluetoothSwitch = findViewById<View>(
                R.id.activity_settings_bluetooth_swith) as CheckBox
        bluetoothSwitch.isChecked = settingsManager.isUseBluetooth
        bluetoothSwitch.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.activity_settings_bluetooth_section) {
            bluetoothSwitch.performClick()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (buttonView.id == R.id.activity_settings_bluetooth_swith) {
            settingsManager.isUseBluetooth = isChecked
        }
    }
}