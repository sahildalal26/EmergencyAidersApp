package com.example.emergencyaidersapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpAiderActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_aider)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Populate gender spinner
        val genderSpinner: Spinner = findViewById(R.id.aiderGenderSpinner)
        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        // Sign up button
        val signUpButton: Button = findViewById(R.id.aiderSignUpButton)
        signUpButton.setOnClickListener { signUpAider() }
    }

    private fun signUpAider() {
        val nameEditText: EditText = findViewById(R.id.aiderNameEditText)
        val usernameEditText: EditText = findViewById(R.id.aiderUsernameEditText)
        val passwordEditText: EditText = findViewById(R.id.aiderPasswordEditText)
        val ageEditText: EditText = findViewById(R.id.aiderAgeEditText)

        val name = nameEditText.text.toString()
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val age = ageEditText.text.toString()
        val gender = findViewById<Spinner>(R.id.aiderGenderSpinner).selectedItem.toString()

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new user with Firebase Authentication
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Save Aider information to Firebase Realtime Database
                    val userId = auth.currentUser?.uid
                    val aiderMap = hashMapOf(
                        "name" to name,
                        "username" to username,
                        "age" to age,
                        "gender" to gender,
                        "firstAidCertificate" to "uploaded_certificate_placeholder" // Handle file upload separately
                    )
                    userId?.let {
                        FirebaseDatabase.getInstance().getReference("Aiders").child(it).setValue(aiderMap)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(this, "Aider signed up successfully!", Toast.LENGTH_SHORT).show()
                                    finish() // Close activity or navigate to another activity
                                } else {
                                    Toast.makeText(this, "Failed to save aider data", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
