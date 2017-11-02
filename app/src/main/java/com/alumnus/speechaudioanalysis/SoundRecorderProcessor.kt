package com.alumnus.speechaudioanalysis

/**
 * Created by sushovan on 27/10/17.
 */

import android.app.PendingIntent.getActivity
import android.content.Context
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.AudioFormat
import android.media.audiofx.NoiseSuppressor
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.AutomaticGainControl
import android.util.Log
import android.widget.Button
import com.alumnus.speechaudioanalysis.R.id.mainButton

import kotlinx.android.synthetic.main.activity_main.*

class SoundRecorderProcessor {
    val audioSampleRate: Int = 44100 //44.1kHz
    private var recorder : AudioRecord? = null
    private var bufferSize:Int = 0
    private var isRecording: Boolean = false
    private val TAG = "SoundRecorderProcessor"

    init {
        try {
            bufferSize = AudioRecord.getMinBufferSize(audioSampleRate,
                    AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT)
            //making the buffer bigger.... [*4] will record 4 seconds of data
            //bufferSize = bufferSize*4;

            isRecording = false
        }catch (e: Exception)
        {
            Log.v(TAG, "Failed to create AudioRecord instance", e)
        }
    }

    fun start() : Boolean {

        try {
            //safe parameters
            recorder = AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION,
                    audioSampleRate, AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize)
        }catch (e: Exception)
        {
            Log.e(TAG, "Couldn't create audioRecord instance", e)
            return false
        }

        try {
            recorder?.startRecording()
            isRecording = true
        }catch (e: Exception)
        {
            Log.e(TAG,"Couldn't start recording Audio stream: " + recorder?.state, e)
            return false
        }

        if(recorder !=  null && recorder?.state == AudioRecord.STATE_INITIALIZED) {
            //explicitly state that the below parameters are safe to be not null using !!
            NoiseSuppressor.create(recorder?.getAudioSessionId()!!)
            AcousticEchoCanceler.create(recorder?.getAudioSessionId()!!)
            AutomaticGainControl.create(recorder?.getAudioSessionId()!!)
        }

        return true
    }

    fun stop() {
        recorder?.stop()
        //release on app exit
        //TODO: check if there's a function for app exit and
        //then release in a later version of the code
        recorder?.release()
        recorder = null
        isRecording = false
    }

    fun isRecording() : Boolean {
        return this.isRecording
    }

    fun getAmplitude() : ShortArray {
        /**
         * This function assumes you have started recording,
         * doesn't start recording by itself
         */

        if(!isRecording)
            return shortArrayOf(0,0)

        //shorten the buffer read size for faster sample collection
        var data =  ShortArray(this.bufferSize, {i->0})
        var average: Double = 0.0
        recorder?.read(data, 0, this.bufferSize)
        //stop()
        var amplitudeRMS = calculateRMS(data)

        var amplitudeDB : Short = 0

        return shortArrayOf(amplitudeRMS, amplitudeDB)

    }

    private fun calculateRMS(data: ShortArray) : Short
    {
        var sumOfSquares: Double = 0.0
        for(num in data)
        {
            sumOfSquares+= num*num
        }
        val meanSquare: Double = sumOfSquares/data.size

        return (Math.sqrt(meanSquare)).toShort()
    }

//    private fun displayAmplitude()
//    {
//        while(this.isRecording)
//        {
//            var currentAmplitude = getAmplitude()
//
//            //this@SoundRecorderProcessor
//
//            findViewById<>(R.id.main_button)
//
//            getActivity().getViewById(R.id.mainButton).setText()
//
//            activity.
//            ((Button)MainButton).setText();
//
//        }
//    }
}