package com.alumnus.speechaudioanalysis

import android.support.v7.app.AppCompatActivity
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
//import android.widget.Button
import android.media.MediaRecorder
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var SoundObject : SoundRecorderProcessor? = null
    //private var textToDisplay = ""
    private val TAG = "MainActivity"
    private val REQUEST_AUDIO = 0
    private val REQUEST_AUDIO_MODIFY = 1
    private var audioPermissionPresent = false
    private var audioModificationPermissionPresent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //@JvmField val mainButton = findViewById<>(R.id.main_button)
        mainButton.setOnClickListener(this)
        mainButton.text = getText(R.string.buttonOFF)

        //check if appropriate sound permissions are present before
        //trying to acquire Audio stream
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED)
        {
            this.audioPermissionPresent = false
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                //TODO:
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        REQUEST_AUDIO);
            }

        }
        else
            this.audioPermissionPresent = true

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS) !=
                PackageManager.PERMISSION_GRANTED)
        {
            this.audioModificationPermissionPresent = false
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
                //TODO:
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.MODIFY_AUDIO_SETTINGS),
                        REQUEST_AUDIO_MODIFY);
            }
        }
        else
            this.audioModificationPermissionPresent = true

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode)
        {
            0 -> {
                this.audioPermissionPresent = true
            }

            1 -> {
                this.audioModificationPermissionPresent = true
            }
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == mainButton.id)
        {
            //TODO: refactor this to create new soundobject on click
            if(SoundObject != null) {
                if (!SoundObject!!.isRecording()) {
                    if(!SoundObject!!.start())
                        Toast.makeText(this, "Couldn't acquire record stream!",
                                Toast.LENGTH_LONG).show()
                    mainButton.text = getText(R.string.buttonON)

                    while (SoundObject!!.isRecording()) {
                        var currentAmplitude = SoundObject!!.getAmplitude()
                        pitchText.text = currentAmplitude[0].toString()
                    }
                } else {
                    SoundObject!!.stop()
                    mainButton.text = getText(R.string.buttonOFF)
                }
            }
            else
            {
                Toast.makeText(this, "Did not get SoundObject",
                        Toast.LENGTH_LONG).show()
            }
        }
    }

}
