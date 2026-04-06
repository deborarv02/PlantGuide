package com.plantguide

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.plantguide.ui.splash.SplashActivity

// Entry point redirect — o launcher real é o SplashActivity declarado no AndroidManifest
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }
}
