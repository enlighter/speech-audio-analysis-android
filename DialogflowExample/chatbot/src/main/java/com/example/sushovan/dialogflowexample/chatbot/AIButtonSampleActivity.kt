package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 20/11/17.
 */

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

import com.google.gson.Gson
import com.google.gson.JsonElement

import java.util.HashMap

import ai.api.android.AIConfiguration
import ai.api.android.GsonFactory
import ai.api.model.AIError
import ai.api.model.AIResponse
import ai.api.model.Metadata
import ai.api.model.Result
import ai.api.model.Status
import ai.api.ui.AIButton

import kotlinx.android.synthetic.main.activity_aibutton_sample.*

class AIButtonSampleActivity : BaseActivity(), AIButton.AIButtonListener {

    private lateinit var aiButton: AIButton
    //private var resultTextView: TextView? = null

    private val gson = GsonFactory.getGson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aibutton_sample)

        //resultTextView = findViewById<View>(R.id.resultTextView)
        aiButton = micButton

        val config = AIConfiguration(Config.ACCESS_TOKEN,
                ai.api.AIConfiguration.SupportedLanguages.EnglishGB,
                AIConfiguration.RecognitionEngine.System)

        config.recognizerStartSound = resources.openRawResourceFd(R.raw.test_start)
        config.recognizerStopSound = resources.openRawResourceFd(R.raw.test_stop)
        config.recognizerCancelSound = resources.openRawResourceFd(R.raw.test_cancel)

        aiButton.initialize(config)
        aiButton.setResultsListener(this)
    }

    override fun onPause() {
        super.onPause()

        // use this method to disconnect from speech recognition service
        // Not destroying the SpeechRecognition object in onPause method
        // would block other apps from using SpeechRecognition service
        aiButton.pause()
    }

    override fun onResume() {
        super.onResume()

        // use this method to reinit connection to recognition service
        aiButton.resume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_aibutton_sample, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId


        if (id == R.id.action_settings) {
            startActivity(AISettingsActivity::class.java)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResult(response: AIResponse) {
        runOnUiThread {
            Log.d(TAG, "onResult")

            resultTextView.text = gson.toJson(response)

            Log.i(TAG, "Received success response")

            // this is example how to get different parts of result object
            val status = response.status
            Log.i(TAG, "Status code: " + status.code!!)
            Log.i(TAG, "Status type: " + status.errorType)

            val result = response.result
            Log.i(TAG, "Resolved query: " + result.resolvedQuery)

            Log.i(TAG, "Action: " + result.action)
            val speech = result.fulfillment.speech
            Log.i(TAG, "Speech: " + speech)
            TTS.speak(speech)

            val metadata = result.metadata
            if (metadata != null) {
                Log.i(TAG, "Intent id: " + metadata.intentId)
                Log.i(TAG, "Intent name: " + metadata.intentName)
            }

            val params = result.parameters
            if (params != null && !params.isEmpty()) {
                Log.i(TAG, "Parameters: ")
                for ((key, value) in params) {
                    Log.i(TAG, String.format("%s: %s", key, value.toString()))
                }
            }
        }
    }

    override fun onError(error: AIError) {
        runOnUiThread {
            Log.d(TAG, "onError")
            resultTextView.text = error.toString()
        }
    }

    override fun onCancelled() {
        runOnUiThread {
            Log.d(TAG, "onCancelled")
            resultTextView.text = ""
        }
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    companion object {

        val TAG = AIButtonSampleActivity::class.java.name
    }
}