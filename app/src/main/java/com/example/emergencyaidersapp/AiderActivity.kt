package com.example.emergencyaidersapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class AiderActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var database: DatabaseReference
    private val emergenciesMap: MutableMap<String, LatLng> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aider)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("emergencies")

        // Start listening for emergencies
        listenForEmergencies()
    }

    private fun listenForEmergencies() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the map to remove previous markers
                mMap.clear()
                emergenciesMap.clear()

                for (emergencySnapshot in snapshot.children) {
                    val emergencyId = emergencySnapshot.key ?: continue
                    val latitude = emergencySnapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                    val longitude = emergencySnapshot.child("longitude").getValue(Double::class.java) ?: 0.0

                    val emergencyLocation = LatLng(latitude, longitude)
                    emergenciesMap[emergencyId] = emergencyLocation

                    // Add a marker for the emergency location
                    mMap.addMarker(MarkerOptions().position(emergencyLocation).title("Emergency Location"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showError("Error fetching emergencies: ${error.message}")
            }
        })
    }

    private fun promptForAcceptance(emergencyId: String, emergencyLocation: LatLng) {
        AlertDialog.Builder(this).apply {
            setTitle("Emergency Alert")
            setMessage("Do you want to accept the emergency and navigate to the location?")
            setPositiveButton("Yes") { _, _ ->
                // Open Google Maps for navigation
                val gmmIntentUri = Uri.parse("google.navigation:q=${emergencyLocation.latitude},${emergencyLocation.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)

                // After navigation, remove the emergency from the database
                removeEmergency(emergencyId)
            }
            setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    private fun removeEmergency(emergencyId: String) {
        database.child(emergencyId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Handle successful deletion, e.g., show a toast
            } else {
                // Handle failure, e.g., show a toast
                showError("Failed to remove emergency.")
            }
        }
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set a map click listener to accept emergencies
        mMap.setOnMarkerClickListener { marker ->
            val emergencyId = emergenciesMap.filterValues { it == marker.position }.keys.firstOrNull()
            if (emergencyId != null) {
                promptForAcceptance(emergencyId, marker.position)
            }
            true
        }
    }
}
