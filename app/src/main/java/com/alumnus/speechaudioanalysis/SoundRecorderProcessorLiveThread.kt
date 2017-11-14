package com.alumnus.speechaudioanalysis

/**
 * Created by sushovan on 30/10/17.
 */

import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.AudioFormat

//TODO: This file is not in use currently

class SoundRecorderProcessorLiveThread: Thread(){
    private var stopped: Boolean

    init {
        stopped = false
        start()
    }

    override fun run()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO)
        var recorder : AudioRecord? = null
        //2D array: val array = Array(row, {IntArray(column)}
        var buffers= Array(256, {ShortArray(160)})
    }
}