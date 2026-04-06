package com.plantguide.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.plantguide.R
import com.plantguide.ui.login.LoginActivity
import com.plantguide.ui.home.HomeActivity
import com.plantguide.util.PreferenceHelper

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = PreferenceHelper(this)
            val destination = if (prefs.isLoggedIn()) {
                Intent(this, HomeActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(destination)
            finish()
        }, 2000L)
    }
}
