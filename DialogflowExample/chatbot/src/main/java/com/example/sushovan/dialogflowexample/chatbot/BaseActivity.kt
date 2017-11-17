package com.example.sushovan.dialogflowexample.chatbot

/**
 * Created by sushovan on 17/11/17.
 */

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

//By default the kotlin activity is final, so we cannot able to extend the class.
// To overcome that we have to make the activity as open so it becomes extendable.
open class BaseActivity : AppCompatActivity() {

    private lateinit var app: AIApplication

    private val handler = Handler()
    //if pauseCallback is accessed before initialization of
    //late-init 'app', it will give UninitializedPropertyAccessException
    //so make it's implementation lazy so that it is initialized only
    //when it is called, by which time oncreate() would have been called
    //and thus 'app' would have been initialized
    private val pauseCallback by lazy {
        Runnable { app.onActivityPaused() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as AIApplication
    }

    override fun onResume() {
        super.onResume()
        app.onActivityResume()
    }

    override fun onPause() {
        super.onPause()
        handler.postDelayed(pauseCallback, PAUSE_CALLBACK_DELAY)
    }

    protected fun checkAudioRecordPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        REQUEST_AUDIO_PERMISSIONS_ID)

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_AUDIO_PERMISSIONS_ID -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    companion object {

        private val PAUSE_CALLBACK_DELAY: Long = 500
        private val REQUEST_AUDIO_PERMISSIONS_ID = 33
    }
}