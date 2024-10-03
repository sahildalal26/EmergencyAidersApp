package com.example.emergencyaidersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons from XML
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignUpUser = findViewById<Button>(R.id.btnSignUpUser)
        val btnSignUpAider = findViewById<Button>(R.id.btnSignUpAider)

        // Login button clicked - go to LoginActivity
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Sign up as a user - go to SignUpActivity for users
        btnSignUpUser.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Sign up as a first aider - go to SignUpActivity for first aiders
       // btnSignUpAider.setOnClickListener {
            //val intent = Intent(this, SignUpAiderActivity::class.java)
           // startActivity(intent)
       // }
    }
}
