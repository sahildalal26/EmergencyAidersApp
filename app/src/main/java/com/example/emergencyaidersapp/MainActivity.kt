package com.example.emergencyaidersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton: Button = findViewById(R.id.loginButton)
        val signUpAiderButton: Button = findViewById(R.id.signUpAiderButton)
        val signUpUserButton: Button = findViewById(R.id.signUpUserButton)

        // Navigate to Login Activity
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Sign Up Aider Activity
        signUpAiderButton.setOnClickListener {
            val intent = Intent(this, SignUpAiderActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Sign Up User Activity
        signUpUserButton.setOnClickListener {
            val intent = Intent(this, SignUpUserActivity::class.java)
            startActivity(intent)
        }
    }
}
