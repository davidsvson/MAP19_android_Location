package com.example.map19_location

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_LOCATION = 1
    private lateinit var locationProviderClient : FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null
    private lateinit var locationcallBack: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationcallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                for (location in locationResult.locations) {
                    println("!!! lat: ${location.latitude} lng: ${location.longitude}")
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            println("!!! no permission")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {
            locationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude
                    println("!!! lat: $lat , lng: $lng")
                }
            }
        }

        locationRequest = createLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.requestLocationUpdates(locationRequest, locationcallBack, Looper.getMainLooper())
        }
    }

    private fun stopLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationcallBack)
    }


    // kotlin version av funktionen
    fun createLocationRequest() =
        LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    /*
    fun createLocationRequest() : LocationRequest {
        var a = LocationRequest.create()
        a.interval = 7000
        a.fastestInterval = 5000
        a.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        return a
    }
    */



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       if(requestCode == REQUEST_LOCATION) {
           if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               startLocationUpdates()
               println("!!! permission granted")
           } else {
               println("!!! permission denied")
           }

       }


       // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
