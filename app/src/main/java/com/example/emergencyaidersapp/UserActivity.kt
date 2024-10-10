package com.example.emergencyaidersapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class UserActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val emergencyButton: Button = findViewById(R.id.emergencyButton)

        emergencyButton.setOnClickListener {
            // Confirm that it's a real emergency
            confirmEmergency()
        }
    }

    private fun confirmEmergency() {
        // This can be a dialog asking for confirmation
        Toast.makeText(this, "Is this a real emergency?", Toast.LENGTH_LONG).show()

        // If confirmed, send the user's location to nearby aiders
        sendEmergencyLocation()
    }

    private fun sendEmergencyLocation() {
        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                // Send this location to nearby aiders
                Toast.makeText(this, "Location: (${location.latitude}, ${location.longitude}) sent!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Unable to get location!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
