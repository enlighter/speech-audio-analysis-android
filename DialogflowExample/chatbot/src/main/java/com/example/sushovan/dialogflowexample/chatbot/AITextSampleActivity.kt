package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 20/11/17.
 */

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView

import com.google.gson.Gson
import com.google.gson.JsonElement

import java.util.Collections
import java.util.HashMap

import ai.api.AIServiceException
import ai.api.RequestExtras
import ai.api.android.AIConfiguration
import ai.api.android.AIDataService
import ai.api.android.GsonFactory
import ai.api.model.AIContext
import ai.api.model.AIError
import ai.api.model.AIEvent
import ai.api.model.AIRequest
import ai.api.model.AIResponse
import ai.api.model.Metadata
import ai.api.model.Result
import ai.api.model.Status

import kotlinx.android.synthetic.main.activity_aitext_sample.*

/**
 * Created by alexey on 07/12/16.
 */
class AITextSampleActivity : BaseActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private val gson = GsonFactory.getGson()

    //private var resultTextView: TextView? = null
    //private var contextEditText: EditText? = null
    //private var queryEditText: EditText? = null
    //private var eventCheckBox: CheckBox? = null

    private lateinit var eventSpinner: Spinner

    private var aiDataService: AIDataService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_aitext_sample)

        //resultTextView = findViewById<View>(R.id.resultTextView) as TextView
        //contextEditText = findViewById<View>(R.id.contextEditText) as EditText
        //queryEditText = findViewById<View>(R.id.textQuery) as EditText

        buttonSend.setOnClickListener(this)
        buttonClear.setOnClickListener(this)

        eventSpinner = selectEventSpinner
        val eventAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, Config.events)
        eventSpinner.adapter = eventAdapter

        //eventCheckBox = findViewById<View>(R.id.eventsCheckBox) as CheckBox
        checkBoxClicked()
        eventsCheckBox.setOnClickListener(this)

    }

    private fun initService() {
        val lang = ai.api.AIConfiguration.SupportedLanguages.EnglishGB
        val config = AIConfiguration(Config.ACCESS_TOKEN,
                lang,
                AIConfiguration.RecognitionEngine.System)


        aiDataService = AIDataService(this, config)
    }


    private fun clearEditText() {
        textQuery.setText("")
    }

    /*
    * AIRequest should have query OR event
    */
    private fun sendRequest() {

        val queryString = if (!eventSpinner.isEnabled) textQuery.text.toString() else null
        val eventString = if (eventSpinner.isEnabled) eventSpinner.selectedItem.
                toString() else null
        val contextString = contextEditText.text.toString()

        if (TextUtils.isEmpty(queryString) && TextUtils.isEmpty(eventString)) {
            onError(AIError(getString(R.string.non_empty_query)))
            return
        }

        val task = object : AsyncTask<String, Void, AIResponse>() {

            private var aiError: AIError? = null

            override fun doInBackground(vararg params: String): AIResponse? {
                val request = AIRequest()
                val query = params[0]
                val event = params[1]

                if (!TextUtils.isEmpty(query))
                    request.setQuery(query)
                if (!TextUtils.isEmpty(event))
                    request.setEvent(AIEvent(event))
                val contextString = params[2]
                var requestExtras: RequestExtras? = null
                if (!TextUtils.isEmpty(contextString)) {
                    val contexts = listOf(AIContext(contextString))
                    requestExtras = RequestExtras(contexts, null)
                }

                try {
                    return aiDataService!!.request(request, requestExtras)
                } catch (e: AIServiceException) {
                    aiError = AIError(e)
                    return null
                }

            }

            override fun onPostExecute(response: AIResponse?) {
                if (response != null) {
                    onResult(response)
                } else {
                    onError(aiError)
                }
            }
        }

        task.execute(queryString, eventString, contextString)
    }

    fun checkBoxClicked() {
        eventSpinner.isEnabled = eventsCheckBox.isChecked
        textQuery.visibility = if (!eventsCheckBox.isChecked) View.VISIBLE else View.GONE
    }


    private fun onResult(response: AIResponse?) {
        runOnUiThread {
            Log.d(TAG, "onResult")

            resultTextView!!.text = gson.toJson(response)

            Log.i(TAG, "Received success response")

            // this is example how to get different parts of result object
            val status = response!!.status
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

    private fun onError(error: AIError?) {
        runOnUiThread { resultTextView!!.text = error!!.toString() }
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

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View,
                                position: Int, id: Long) {
        //val selectedLanguage = parent.getItemAtPosition(position) as LanguageConfig
        initService()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonClear -> clearEditText()
            R.id.buttonSend -> sendRequest()
            R.id.eventsCheckBox -> checkBoxClicked()
        }
    }

    companion object {

        val TAG = AITextSampleActivity::class.java.name
    }
}