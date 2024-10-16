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
import com.google.firebase.database.FirebaseDatabase

class UserActivity : ComponentActivity() {

    private lateinit var emergencyButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        emergencyButton = findViewById(R.id.emergencyButton)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        emergencyButton.setOnClickListener {
            sendEmergencyLocation()
        }
    }

    private fun sendEmergencyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val emergencyId = FirebaseDatabase.getInstance().getReference("emergencies").push().key
                    val emergencyLocation = EmergencyLocation(location.latitude, location.longitude)

                    if (emergencyId != null) {
                        FirebaseDatabase.getInstance().getReference("emergencies").child(emergencyId)
                            .setValue(emergencyLocation)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Emergency location sent!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Failed to send location: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to get location: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

data class EmergencyLocation(val latitude: Double, val longitude: Double)

