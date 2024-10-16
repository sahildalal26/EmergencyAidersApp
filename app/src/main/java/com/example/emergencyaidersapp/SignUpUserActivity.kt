package com.example.emergencyaidersapp

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

class SignUpUserActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_user)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Populate gender spinner
        val genderSpinner: Spinner = findViewById(R.id.userGenderSpinner)
        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        // Sign up button
        val signUpButton: Button = findViewById(R.id.userSignUpButton)
        signUpButton.setOnClickListener { signUpUser() }
    }

    private fun signUpUser() {
        val nameEditText: EditText = findViewById(R.id.userNameEditText)
        val ageEditText: EditText = findViewById(R.id.userAgeEditText)
        val usernameEditText: EditText = findViewById(R.id.userUsernameEditText)
        val passwordEditText: EditText = findViewById(R.id.userPasswordEditText)
        val medicalConditionEditText: EditText = findViewById(R.id.userMedicalConditionEditText)

        val name = nameEditText.text.toString()
        val age = ageEditText.text.toString()
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val medicalCondition = medicalConditionEditText.text.toString()
        val gender = findViewById<Spinner>(R.id.userGenderSpinner).selectedItem.toString()

        if (name.isEmpty() || age.isEmpty() || username.isEmpty() || password.isEmpty() || medicalCondition.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new user with Firebase Authentication
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Save user information to Firebase Realtime Database
                    val userId = auth.currentUser?.uid
                    val userMap = hashMapOf(
                        "name" to name,
                        "age" to age,
                        "username" to username,
                        "gender" to gender,
                        "medicalCondition" to medicalCondition
                    )
                    userId?.let {
                        FirebaseDatabase.getInstance().getReference("Users").child(it).setValue(userMap)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(this, "User signed up successfully!", Toast.LENGTH_SHORT).show()
                                    finish() // Close activity or navigate to another activity
                                } else {
                                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
