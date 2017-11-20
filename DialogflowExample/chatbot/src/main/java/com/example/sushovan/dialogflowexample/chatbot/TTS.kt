package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 20/11/17.
 */

import android.content.Context
import android.speech.tts.TextToSpeech

object TTS {

    private var textToSpeech: TextToSpeech? = null

    fun init(context: Context) {
        if (textToSpeech == null) {
            textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener { })
        }
    }

    fun speak(text: String) {
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}

