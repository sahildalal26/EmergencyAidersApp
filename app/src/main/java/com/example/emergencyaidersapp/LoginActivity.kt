package com.example.emergencyaidersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : ComponentActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.loginEmailEditText)
        passwordEditText = findViewById(R.id.loginPasswordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkUserRole(auth.currentUser?.uid ?: return@addOnCompleteListener)
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserRole(userId: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        val aiderRef = FirebaseDatabase.getInstance().getReference("Aiders").child(userId)

        // Check if user exists in the 'users' node
        userRef.get().addOnSuccessListener { userSnapshot ->
            if (userSnapshot.exists()) {
                // User found in the users node
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish() // Close the LoginActivity
            } else {
                // Check if user exists in the 'aiders' node
                aiderRef.get().addOnSuccessListener { aiderSnapshot ->
                    if (aiderSnapshot.exists()) {
                        // User found in the aiders node
                        val intent = Intent(this, AiderActivity::class.java)
                        startActivity(intent)
                        finish() // Close the LoginActivity
                    } else {
                        // User not found in either node
                        Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to check Aider role.", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            // Handle failure to retrieve data
            Toast.makeText(this, "Failed to check User role.", Toast.LENGTH_SHORT).show()
        }
    }
}
