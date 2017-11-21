package com.alumnus.speechaudioanalysis

import android.support.v7.app.AppCompatActivity
import android.support.v4.app.ActivityCompat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var SoundObject : SoundRecorderProcessor? = null
    private val TAG = MainActivity::class.java.name
    private val IDLE = 0
    private val RUNNING = 1
    private val requestIdRequiredAudioPermissions = 1
    private val audioPermissions = arrayOf(Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS)
    private var hasRequiredAudioPermissions = false
    private var audioProcessThread: Thread? = null
    private var currentState = IDLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //@JvmField val mainButton = findViewById<>(R.id.main_button)
        mainButton.setOnClickListener(this)
        mainButton.text = getText(R.string.buttonOFF)

        //check if appropriate sound permissions are present before
        //trying to acquire Audio stream
        if(!hasPermissions(this, audioPermissions))
        {
            //TODO: Explain why these permissions are needed

            //Then request permission
            ActivityCompat.requestPermissions(this, audioPermissions,
                    requestIdRequiredAudioPermissions)
        }

    }

    fun hasPermissions(context: Context, permissions: Array<out String>) : Boolean {
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
            }
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == mainButton.id)
        {
            if(!this.hasRequiredAudioPermissions)
            {
                Toast.makeText(this, "Without required audio permissions, " +
                        "this app cannot function", Toast.LENGTH_LONG).show()
                this.currentState = IDLE
                return
            }

            this.currentState = RUNNING

            if(SoundObject == null)
                SoundObject = SoundRecorderProcessor()

            if(SoundObject != null) {
                if (!SoundObject!!.isRecording()) {
                    if(!SoundObject!!.start())
                        Toast.makeText(this, "Couldn't acquire record stream!",
                                Toast.LENGTH_LONG).show()
                    mainButton.text = getText(R.string.buttonON)

                    //while (SoundObject!!.isRecording()) {
//                        var currentAmplitude = SoundObject!!.getAmplitude()
//                        amplitudeText.text = currentAmplitude[0].toString()
//                        var currentPitch = SoundObject!!.getPitch()
//                        pitchText.text = currentPitch.toString()
                    //}

                    if (this.audioProcessThread == null) {
                        this.audioProcessThread = Thread { runOnAudioProcessThread() }
                        this.audioProcessThread?.start()
                    }

                } else {
                    this.currentState = IDLE
                    SoundObject!!.stop()
                    mainButton.text = getText(R.string.buttonOFF)
                    resetDisplayValues()
                }
            }
            else
            {
                Toast.makeText(this, "Did not get SoundObject",
                        Toast.LENGTH_LONG).show()
                this.currentState = IDLE
            }
        }
    }

    private fun runOnAudioProcessThread()
    {
        while (SoundObject!!.isRecording() && this.currentState == RUNNING)
        {
            var currentAmplitude = SoundObject!!.getAmplitude()
            Log.d(TAG, "Current amplitude: " + currentAmplitude.toString())
            var currentPitch = SoundObject!!.getPitch()
            Log.d(TAG,"Current pitch: " + currentPitch.toString())

            runOnUiThread { displayAcquiredValues(currentPitch, currentAmplitude) }

        }
    }

    private fun displayAcquiredValues(pitchInHz: Float,
                                      amplitude: SoundRecorderProcessor.amplitudeResult)
    /**
     * assumes SoundObject is not null,
     * so handle this accordingly
     */
    {
        pitchText.text = pitchInHz.toString()
        val (ampRMS, DBrms) = amplitude
        amplitudeText.text = ampRMS.toString()
    }

    private fun resetDisplayValues()
    {
        displayAcquiredValues(0f,
                SoundRecorderProcessor.amplitudeResult(0,0.0))
    }

}
