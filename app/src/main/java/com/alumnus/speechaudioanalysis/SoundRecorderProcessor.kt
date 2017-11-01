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
import android.widget.Toast
import com.alumnus.speechaudioanalysis.R.id.mainButton

import kotlinx.android.synthetic.main.activity_main.*

class SoundRecorderProcessor {
    private val sampleRates = intArrayOf(8000, 11025, 22050, 44100)
    val audioSampleRate: Int = 44100 //44.1kHz
    private var recorder : AudioRecord? = null
    private var chosenRate = 0
    private var chosenFormat = 0
    private var chosenChannel = 0
    private var bufferSize:Int = 0
    private var isRecording: Boolean = false
    private val TAG = "SoundRecorderProcessor"

    data class audioSettings(val recorder: AudioRecord?, val rate: Int, val format: Int,
                             val channel: Int)

    init {
        try {
            bufferSize = AudioRecord.getMinBufferSize(audioSampleRate,
                    AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT)
            //making the buffer bigger.... [*4] will record data of longer time quantum
            //bufferSize = bufferSize*4;
            val (Re, Ra, Fo, Ch) = findAudioRecord()
            recorder = Re
            chosenRate = Ra
            chosenFormat = Fo
            chosenChannel = Ch
            isRecording = false
        }catch (e: Exception)
        {
            Log.v(TAG, "Failed to create AudioRecord instance", e)
        }
    }

    private fun findAudioRecord(): audioSettings
    {
        val audioFormats = intArrayOf(AudioFormat.ENCODING_PCM_8BIT,
                AudioFormat.ENCODING_PCM_16BIT)
        val channelConfigs = intArrayOf(AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.CHANNEL_IN_STEREO)
        for (rate in sampleRates)
        {
            for (format in audioFormats)
            {
                for (channel in channelConfigs)
                {
                    try {
                        Log.d(TAG, "Attempting rate " + rate.toString()
                                + "Hz, bits: " + format.toString() + ", channel: "
                                + channel.toString())
                        bufferSize = AudioRecord.getMinBufferSize(rate,
                                channel, format)

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            var recorderInstance = AudioRecord(
                                    MediaRecorder.AudioSource.VOICE_RECOGNITION, rate,
                                    channel, format, bufferSize)

                            if (recorderInstance?.state == AudioRecord.STATE_INITIALIZED)
                                return audioSettings(recorderInstance, rate, format, channel)
                        }

                    }catch (e: Exception)
                    {
                        Log.e(TAG, rate.toString() + ": Exception, keep trying.",e)
                    }
                }
            }
        }

        return audioSettings(null,0,0,0)
    }

    fun start(): Boolean {
        if (recorder == null)
        {
            return false
        }
        try {
            recorder?.startRecording()
            isRecording = true
            return true
        }catch (e: Exception)
        {
            Log.v(TAG,"Couldn't start recording Audio stream: " + recorder?.state, e)
            return false
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