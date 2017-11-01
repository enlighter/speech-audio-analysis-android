package com.alumnus.speechaudioanalysis

import android.support.v7.app.AppCompatActivity
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
    //private var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //@JvmField val mainButton = findViewById<>(R.id.main_button)
        mainButton.setOnClickListener(this)
        mainButton.text = getText(R.string.buttonOFF)

        SoundObject = SoundRecorderProcessor()

    }

    override fun onClick(v: View?) {
        if(v?.id == mainButton.id)
        {
            if(SoundObject != null) {
                if (!SoundObject!!.isRecording()) {
                    SoundObject!!.start()
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
