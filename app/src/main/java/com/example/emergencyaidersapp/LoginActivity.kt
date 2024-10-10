package com.example.emergencyaidersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.emergencyaidersapp.database.DatabaseHelper
//import com.example.emergencyaidersapp.database.com.example.emergencyaidersapp.UserActivity

class LoginActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        databaseHelper = DatabaseHelper(this)

        val usernameEditText: EditText = findViewById(R.id.loginUsernameEditText)
        val passwordEditText: EditText = findViewById(R.id.loginPasswordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both username and password", Toast.LENGTH_SHORT).show()
            } else {
                val isUserValid = databaseHelper.checkUser(username, password)
                if (isUserValid) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    // Check if user is an aider or regular user
                    navigateToCorrectActivity(username)
                } else {
                    Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToCorrectActivity(username: String) {
        // Assuming we have a way to differentiate between User and Aider
        // Here we simply check based on username, but ideally, you'd store user roles in the database.

        // For example, if the username contains "aider", treat it as an Aider
        if (username.contains("aider", ignoreCase = true)) {
            val intent = Intent(this, AiderActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
    }
}
