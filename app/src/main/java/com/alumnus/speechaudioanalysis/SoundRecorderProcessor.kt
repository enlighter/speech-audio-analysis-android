package com.alumnus.speechaudioanalysis

/**
 * Created by sushovan on 27/10/17.
 */

import android.media.MediaRecorder

class SoundRecorderProcessor {
    private var recorder : MediaRecorder? = null

    fun start() {
        if(recorder == null) {
            recorder = MediaRecorder()
            recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder?.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD)
            recorder?.setOutputFile("/dev/null")
            recorder?.prepare()
            recorder?.start()
        }
    }

    fun stop() {
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    fun getState() : Boolean {
        //if state is true then recorder is free, false means busy
        return this.recorder == null
    }

    fun getMaxAmplitude() : Int {
        return recorder?.maxAmplitude ?: 0
    }
}