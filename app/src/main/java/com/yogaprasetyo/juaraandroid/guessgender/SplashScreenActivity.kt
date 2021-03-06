package com.yogaprasetyo.juaraandroid.guessgender

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yogaprasetyo.juaraandroid.guessgender.ui.MainActivity

/**
 * Handle splash screen for first open app
 * this splash screen work on Android 12
 * */
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }
}