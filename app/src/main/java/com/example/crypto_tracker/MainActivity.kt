package com.example.crypto_tracker;

import android.app.Activity;
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)



        //4second splash time
        Handler().postDelayed({
            textview_bit.visibility = View.VISIBLE
            //start main activity
            startActivity(Intent(this@MainActivity, crypto::class.java))
            //finish this activity
            finish()
        },4000)
    }

}
