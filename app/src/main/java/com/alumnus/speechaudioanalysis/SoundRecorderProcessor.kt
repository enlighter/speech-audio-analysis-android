package com.alumnus.speechaudioanalysis

/**
 * Created by sushovan on 27/10/17.
 */

import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.AudioFormat

class SoundRecorderProcessor {
    val audioSampleRate: Int
    private var recorder : AudioRecord? = null
    private var bufferSize:Int
    private var isRecording: Boolean

    init {
        audioSampleRate = 44100 //44.1kHz
        bufferSize = AudioRecord.getMinBufferSize(audioSampleRate,
                AudioFormat.CHANNEL_IN_DEFAULT,AudioFormat.ENCODING_PCM_16BIT)
        //making the buffer bigger....
        bufferSize = bufferSize*4;
        recorder = AudioRecord(MediaRecorder.AudioSource.MIC,
                audioSampleRate, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize)
        isRecording = false
    }

    fun start() {
        recorder?.startRecording()
        isRecording = true
    }

    fun stop() {
        recorder?.stop()
        isRecording = false
    }

    fun isRecording() : Boolean {
        //if state is true then recorder is free, false means busy
        return this.isRecording
    }

    fun getMaxAmplitude() : Int {
        return 0
    }
}