package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 17/11/17.
 */

import android.content.Context
import android.speech.tts.TextToSpeech

class TTS(context: Context) {

    private var textToSpeech: TextToSpeech =
            TextToSpeech(context, TextToSpeech.OnInitListener { })

    fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}