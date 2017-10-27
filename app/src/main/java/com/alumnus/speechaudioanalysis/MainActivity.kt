package com.alumnus.speechaudioanalysis

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
//import android.widget.Button
import android.media.MediaRecorder
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //@JvmField val mainButton = findViewById<>(R.id.main_button)
        mainButton.setOnClickListener(this)

        // Example of a call to a native method
        //sample_text.text = stringFromJNI()
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
