package com.example.emergencyaidersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpAiderButton = findViewById<Button>(R.id.signUpAiderButton)
        val signUpUserButton = findViewById<Button>(R.id.signUpUserButton)

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signUpAiderButton.setOnClickListener {
            startActivity(Intent(this, SignUpAiderActivity::class.java))
        }

        signUpUserButton.setOnClickListener {
            startActivity(Intent(this, SignUpUserActivity::class.java))
        }
    }
}
