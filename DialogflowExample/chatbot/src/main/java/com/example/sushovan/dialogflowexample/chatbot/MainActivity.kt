package com.example.sushovan.dialogflowexample.chatbot

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    /*private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        TTS.init(applicationContext)
    }

    override fun onStart() {
        super.onStart()

        checkAudioRecordPermission()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()
        if (id == R.id.action_settings) {
            //val intent = Intent(this, AISettingsActivity::class.java)
            startActivity(AISettingsActivity::class.java)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun serviceSampleClick(view: View) {
        //val intent = Intent(this, AIServiceSampleActivity::class.java)
        startActivity(AIServiceSampleActivity::class.java)
    }

    fun buttonSampleClick(view: View) {
        startActivity(AIButtonSampleActivity::class.java)
    }

    fun dialogSampleClick(view: View) {
        startActivity(AIDialogSampleActivity::class.java)
    }

    fun textSampleClick(view: View) {
        startActivity(AITextSampleActivity::class.java)
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    companion object {

        private val TAG = AIApplication::class.java.name
    }

}
