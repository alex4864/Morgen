package com.turtledevs.alex.morgen

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var locationListener: MyLocationListener = MyLocationListener(this)
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestLocationUpdates("gps", 5000, 1.0f, locationListener)
        }
        catch (error: SecurityException) {
            Log.e(TAG, error.message)
        }
    }

    fun toSetDestination(view: View) {
        intent = Intent(this, SetDestinationActivity::class.java)
        startActivity(intent)
    }

    fun updateLocation(view: View) {
        findViewById<TextView>(R.id.Location_Field).apply {
            var location = locationListener.lastLocation
            if (location != null) {
                text = "Latitude: " + location.getLatitude() + '\n' + "Longitude: " + location.getLongitude()
            }
            else {
                text = "No location data available at this time."
            }
        }
    }
}

class MyLocationListener(activity: AppCompatActivity) : LocationListener {
    private val TAG: String = "MyLocationListener"

    var activity: AppCompatActivity = activity
    var lastLocation: Location? = null
    var lastStatus: Int? = null
    var providerEnabled: Boolean? = null

    override fun onLocationChanged(location: Location) {
        lastLocation = location

        activity.findViewById<TextView>(R.id.Location_Field).apply {
            text = "Latitude: %f\nLongitude: %f".format(location.getLatitude(), location.getLongitude())
        }

        Log.v(TAG, "Location Changed")
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        lastStatus = status

        Log.v(TAG, "Status Changed To: $status")
    }

    override fun onProviderEnabled(provider: String) {
        providerEnabled = true

        Log.v(TAG, "Provider Enabled")
    }
    override fun onProviderDisabled(provider: String) {
        providerEnabled = false

        Log.v(TAG, "Provider Disabled")
    }
}