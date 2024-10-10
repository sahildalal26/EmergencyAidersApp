package com.example.emergencyaidersapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class SignUpAiderActivity : ComponentActivity() {

    private val PICK_CERTIFICATE_REQUEST = 1
    private var certificateUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_aider)

        val nameEditText: EditText = findViewById(R.id.aiderNameEditText)
        val emailEditText: EditText = findViewById(R.id.aiderEmailEditText)
        val phoneEditText: EditText = findViewById(R.id.aiderPhoneEditText)
        val uploadCertificateButton: Button = findViewById(R.id.uploadCertificateButton)
        val submitButton: Button = findViewById(R.id.submitButton)

        // Handle certificate upload
        uploadCertificateButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf" // Restrict to PDF files
            startActivityForResult(intent, PICK_CERTIFICATE_REQUEST)
        }

        // Handle submission
        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || certificateUri == null) {
                Toast.makeText(this, "Please fill in all details and upload the certificate", Toast.LENGTH_LONG).show()
            } else {
                // Here you would handle saving the data to your database or backend
                // For now, we'll just show a toast message
                Toast.makeText(this, "Account pending confirmation", Toast.LENGTH_LONG).show()
                // Go back to the main activity
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_CERTIFICATE_REQUEST && resultCode == Activity.RESULT_OK) {
            certificateUri = data?.data
            if (certificateUri != null) {
                Toast.makeText(this, "Certificate uploaded", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
