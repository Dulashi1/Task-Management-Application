package com.example.labexam4

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        // Creating an intent to start the MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Using Handler to delay the start of MainActivity
        Handler().postDelayed({
            startActivity(intent)
            finish() // Finish the SplashActivity after starting MainActivity
        }, 1000) // Delay in milliseconds
    }
}
