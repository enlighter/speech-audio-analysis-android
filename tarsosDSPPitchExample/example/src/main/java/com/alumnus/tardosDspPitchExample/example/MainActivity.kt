package com.alumnus.tarsosDspPitchExample.example

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import be.tarsos.dsp.pitch.PitchProcessor
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var hasRequiredAudioPermissions = false
    private val audioPermissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val requestIdRequiredAudioPermissions = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check if appropriate sound permissions are present before
        //trying to acquire Audio stream
        if(!hasPermissions(this, audioPermissions))
        {
            //TODO: Explain why these permissions are needed

            //Then request permission
            ActivityCompat.requestPermissions(this, audioPermissions,
                    requestIdRequiredAudioPermissions)
        }
        else
            showPitch()
    }

    fun hasPermissions(context: Context, permissions: Array<out String>) : Boolean
    {
        for (permission in permissions)
        {
            if(ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED)
            {
                this.hasRequiredAudioPermissions = false
                return false
            }
        }

        this.hasRequiredAudioPermissions = true
        return true
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode)
        {
            1 -> {
                this.hasRequiredAudioPermissions = true
                showPitch()
            }
        }
    }

    fun showPitch()
    {
        val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(
                22050,1024,0)
        var pdh = object : PitchDetectionHandler {
            override fun handlePitch(p0: PitchDetectionResult?, p1: AudioEvent?) {
                val pitchInHz = p0?.getPitch()

                runOnUiThread { processPitch(pitchInHz!!) }
            }
        }

        val pitchProcessor = PitchProcessor(PitchEstimationAlgorithm.FFT_YIN,
                22050f, 1024, pdh)
        dispatcher.addAudioProcessor(pitchProcessor)

        val audioThread = Thread(dispatcher, "Audio Thread")
        audioThread.start()
    }

    fun processPitch(pitchInHz: Float) {

        pitchText.setText("" + pitchInHz)

        if (pitchInHz >= 110 && pitchInHz < 123.47) {
            //A
            noteText.setText("A")
        } else if (pitchInHz >= 123.47 && pitchInHz < 130.81) {
            //B
            noteText.setText("B")
        } else if (pitchInHz >= 130.81 && pitchInHz < 146.83) {
            //C
            noteText.setText("C")
        } else if (pitchInHz >= 146.83 && pitchInHz < 164.81) {
            //D
            noteText.setText("D")
        } else if (pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            //E
            noteText.setText("E")
        } else if (pitchInHz >= 174.61 && pitchInHz < 185) {
            //F
            noteText.setText("F")
        } else if (pitchInHz >= 185 && pitchInHz < 196) {
            //G
            noteText.setText("G")
        }
    }
}
