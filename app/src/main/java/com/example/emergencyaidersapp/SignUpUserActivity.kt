package com.example.emergencyaidersapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.emergencyaidersapp.database.DatabaseHelper // Assuming you have a database helper class to store user data

class SignUpUserActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_user)

        databaseHelper = DatabaseHelper(this)  // Initialize the database helper

        val usernameEditText: EditText = findViewById(R.id.userUsernameEditText)
        val passwordEditText: EditText = findViewById(R.id.userPasswordEditText)
        val medicalConditionEditText: EditText = findViewById(R.id.userMedicalConditionEditText)
        val signUpButton: Button = findViewById(R.id.userSignUpButton)

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val medicalCondition = medicalConditionEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_LONG).show()
            } else {
                // Save user data to the database
                val success = databaseHelper.insertUser(username, password, medicalCondition)
                if (success) {
                    Toast.makeText(this, "User signed up successfully!", Toast.LENGTH_LONG).show()
                    // Go back to the main activity or login screen
                    finish()
                } else {
                    Toast.makeText(this, "Error signing up. Try again.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
