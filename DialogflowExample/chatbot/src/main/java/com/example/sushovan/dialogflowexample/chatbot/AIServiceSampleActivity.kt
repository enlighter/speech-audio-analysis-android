package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 17/11/17.
 */

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
//import android.widget.Spinner;
import android.widget.TextView

import com.google.gson.Gson
import com.google.gson.JsonElement

import java.util.Collections
import java.util.HashMap

import ai.api.android.AIConfiguration
import ai.api.AIListener
import ai.api.android.AIService
import ai.api.android.GsonFactory
import ai.api.RequestExtras
import ai.api.model.AIContext
import ai.api.model.AIError
import ai.api.model.AIResponse
import ai.api.model.Metadata
import ai.api.model.Result
import ai.api.model.Status

import kotlinx.android.synthetic.main.activity_aiservice_sample.*

class AIServiceSampleActivity : BaseActivity(), AIListener {

    private var aiService: AIService? = null
    //private var progressBar: ProgressBar? = null
    //private var recIndicator: ImageView? = null
    //private var resultTextView: TextView? = null
    //private var contextEditText: EditText? = null

    private val gson = GsonFactory.getGson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aiservice_sample)

        //progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        //recIndicator = findViewById<View>(R.id.recIndicator) as ImageView
        //resultTextView = findViewById<View>(R.id.resultTextView) as TextView
        //contextEditText = findViewById<View>(R.id.contextEditText) as EditText

    }

    private fun initService() {
        //final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag(selectedLanguage.getLanguageCode());
        val config = AIConfiguration(CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.EnglishGB,
                AIConfiguration.RecognitionEngine.System)

        if (aiService != null) {
            aiService!!.pause()
        }

        aiService = AIService.getService(this, config)
        aiService!!.setListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_aiservice_sample, menu)
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

    override fun onPause() {
        super.onPause()

        // use this method to disconnect from speech recognition service
        // Not destroying the SpeechRecognition object in onPause method would block other apps from using SpeechRecognition service
        if (aiService != null) {
            aiService!!.pause()
        }
    }

    override fun onResume() {
        super.onResume()

        // use this method to reinit connection to recognition service
        if (aiService != null) {
            aiService!!.resume()
        }
    }

    fun startRecognition(view: View) {
        val contextString = contextEditText.text.toString()
        if (TextUtils.isEmpty(contextString)) {
            aiService!!.startListening()
        } else {
            val contexts = listOf(AIContext(contextString))
            val requestExtras = RequestExtras(contexts, null)
            aiService!!.startListening(requestExtras)
        }

    }

    fun stopRecognition(view: View) {
        aiService!!.stopListening()
    }

    fun cancelRecognition(view: View) {
        aiService!!.cancel()
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
        runOnUiThread { resultTextView.text = error.toString() }
    }

    override fun onAudioLevel(level: Float) {
        runOnUiThread {
            var positiveLevel = Math.abs(level)

            if (positiveLevel > 100) {
                positiveLevel = 100f
            }
            progressBar.progress = positiveLevel.toInt()
        }
    }

    override fun onListeningStarted() {
        runOnUiThread { recIndicator.visibility = View.VISIBLE }
    }

    override fun onListeningCanceled() {
        runOnUiThread {
            recIndicator.visibility = View.INVISIBLE
            resultTextView.text = ""
        }
    }

    override fun onListeningFinished() {
        runOnUiThread { recIndicator.visibility = View.INVISIBLE }
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    companion object {

        val TAG = AIServiceSampleActivity::class.java.name
        //TODO: enter the client access token of dialogflow agent
        val CLIENT_ACCESS_TOKEN = ""
    }
}