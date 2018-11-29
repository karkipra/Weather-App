package com.pratikkarki.weatherapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        imLogo.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.logo_anim))
        tvAppName.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.title_anim))

        Handler().postDelayed({

            var intentStart = Intent()
            intentStart.setClass(this@SplashActivity, MainActivity::class.java)
            startActivity(intentStart)
        }, 3000)
    }
}
