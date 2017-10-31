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
import android.widget.Button
import com.alumnus.speechaudioanalysis.R.id.mainButton

import kotlinx.android.synthetic.main.activity_main.*

class SoundRecorderProcessor {
    val audioSampleRate: Int
    private var recorder : AudioRecord? = null
    private var bufferSize:Int
    private var isRecording: Boolean

    init {
        audioSampleRate = 44100 //44.1kHz
        bufferSize = AudioRecord.getMinBufferSize(audioSampleRate,
                AudioFormat.CHANNEL_IN_DEFAULT,AudioFormat.ENCODING_PCM_16BIT)
        //making the buffer bigger.... [*4] will record 4 seconds of data
        //bufferSize = bufferSize*4;
        recorder = AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION,
                audioSampleRate, AudioFormat.CHANNEL_IN_MONO, //mono buffer can be processed linearly,
                //safer option
                AudioFormat.ENCODING_PCM_16BIT, bufferSize)
        isRecording = false
    }

    fun start() {
        try {
            recorder?.startRecording()
            isRecording = true
        }catch (e: Exception)
        {
            //TODO: log this!
        }

        if(recorder !=  null) {
            //explicitly state that the below parameters are safe to be not null using !!
            NoiseSuppressor.create(recorder?.getAudioSessionId()!!)
            AcousticEchoCanceler.create(recorder?.getAudioSessionId()!!)
            AutomaticGainControl.create(recorder?.getAudioSessionId()!!)
        }

    }

    fun stop() {
        recorder?.stop()
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