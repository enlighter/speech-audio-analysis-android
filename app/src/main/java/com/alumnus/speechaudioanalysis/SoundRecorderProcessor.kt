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

    init {
        audioSampleRate = 44100 //44.1kHz
        bufferSize = AudioRecord.getMinBufferSize(audioSampleRate,
                AudioFormat.CHANNEL_IN_DEFAULT,AudioFormat.ENCODING_PCM_16BIT)
        //making the buffer bigger....
        bufferSize = bufferSize*4;
        recorder = AudioRecord(MediaRecorder.AudioSource.MIC,
                audioSampleRate, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize)
    }

    fun start() {
        recorder?.startRecording()
    }

    fun stop() {
        recorder?.stop()
    }

    fun getState() : Boolean {
        //if state is true then recorder is free, false means busy
        return this.recorder == null
    }

    fun getMaxAmplitude() : Int {
        return 0
    }
}