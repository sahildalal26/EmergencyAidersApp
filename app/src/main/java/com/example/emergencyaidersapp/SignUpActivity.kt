package com.example.emergencyaidersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupEmail: EditText = findViewById(R.id.signupEmail)
        val signupPassword: EditText = findViewById(R.id.signupPassword)
        val signupButton: Button = findViewById(R.id.signupButton)

        // Handle sign up button click
        signupButton.setOnClickListener {
            val email = signupEmail.text.toString()
            val password = signupPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Add logic to create a new user account (registration)
                Toast.makeText(this, "Signed up!", Toast.LENGTH_SHORT).show()
                // Navigate back to Login Activity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
