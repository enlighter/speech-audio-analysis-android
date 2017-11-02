package com.alumnus.tardosDspPitchExample.example

/**
 * Created by sushovan on 2/11/17.
 */

import android.media.AudioRecord
import android.media.MediaRecorder
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.PipedAudioStream
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.TarsosDSPAudioInputStream
import be.tarsos.dsp.io.android.AndroidAudioInputStream

/**
 * The Factory creates [AudioDispatcher] objects from the
 * configured default microphone of an Android device.
 * It depends on the android runtime and does not work on the standard Java runtime.
 *
 * @author Joren Six
 * @see AudioDispatcher
 */
object AudioDispatcherFactory {

    /**
     * Create a new AudioDispatcher connected to the default microphone.
     *
     * @param sampleRate
     * The requested sample rate.
     * @param audioBufferSize
     * The size of the audio buffer (in samples).
     *
     * @param bufferOverlap
     * The size of the overlap (in samples).
     * @return A new AudioDispatcher
     */
    fun fromDefaultMicrophone(sampleRate: Int,
                              audioBufferSize: Int, bufferOverlap: Int): AudioDispatcher {
        val minAudioBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                android.media.AudioFormat.CHANNEL_IN_MONO,
                android.media.AudioFormat.ENCODING_PCM_16BIT)
        val minAudioBufferSizeInSamples = minAudioBufferSize / 2
        if (minAudioBufferSizeInSamples <= audioBufferSize) {
            val audioInputStream = AudioRecord(
                    MediaRecorder.AudioSource.MIC, sampleRate,
                    android.media.AudioFormat.CHANNEL_IN_MONO,
                    android.media.AudioFormat.ENCODING_PCM_16BIT,
                    audioBufferSize * 2)

            val format = TarsosDSPAudioFormat(sampleRate.toFloat(), 16, 1, true, false)

            val audioStream = AndroidAudioInputStream(audioInputStream, format)
            //start recording ! Opens the stream.
            audioInputStream.startRecording()
            return AudioDispatcher(audioStream, audioBufferSize, bufferOverlap)
        } else {
            throw IllegalArgumentException("Buffer size too small should be at least " + minAudioBufferSize * 2)
        }
    }


    /**
     * Create a stream from a piped sub process and use that to create a new
     * [AudioDispatcher] The sub-process writes a WAV-header and
     * PCM-samples to standard out. The header is ignored and the PCM samples
     * are are captured and interpreted. Examples of executables that can
     * convert audio in any format and write to stdout are ffmpeg and avconv.
     *
     * @param source
     * The file or stream to capture.
     * @param targetSampleRate
     * The target sample rate.
     * @param audioBufferSize
     * The number of samples used in the buffer.
     * @param bufferOverlap
     * The number of samples to overlap the current and previous buffer.
     * @return A new audioprocessor.
     */
    fun fromPipe(source: String, targetSampleRate: Int, audioBufferSize: Int, bufferOverlap: Int): AudioDispatcher {
        val f = PipedAudioStream(source)
        val audioStream = f.getMonoStream(targetSampleRate, 0.0)
        return AudioDispatcher(audioStream, audioBufferSize, bufferOverlap)
    }
}