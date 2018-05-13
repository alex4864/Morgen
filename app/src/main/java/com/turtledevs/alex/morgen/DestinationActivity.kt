package com.turtledevs.alex.morgen

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DestinationActivity() : AppCompatActivity(), OnMapReadyCallback {
    private val TAG: String = "DestinationActivity"

    lateinit var destination: Location
    var userLocation: Location? = null
    var proximity: Float = 20.0f

    var locationListener: LocationListener = DestinationLocationListener(this)
    var destinatonReached: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)

        destination = constructLocation(intent.getDoubleExtra("LATITUDE", 0.0), intent.getDoubleExtra("LONGITUDE", 0.0))
        proximity = intent.getFloatExtra("PROXIMITY", 20.0f )

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestLocationUpdates("gps", 5000, 1.0f, locationListener)
        }
        catch (error: SecurityException) {
            Log.e(TAG, error.message)
        }

        var mapFragment: SupportMapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun onLocationChange(location: Location) {
        if (location.distanceTo(destination) <= proximity) {
            destinatonReached = true
            findViewById<TextView>(R.id.statusText).apply {
                text = "Status: Arrived"
            }

            Log.v(TAG, "Destination Reached")
        }

        findViewById<TextView>(R.id.distanceText).apply {
            text = "Distance: %f".format(location.distanceTo(destination))
        }

        userLocation = location
    }

    override fun onMapReady(map: GoogleMap) {
        val graz = LatLng(47.070714, 15.439504)
        map.addMarker(MarkerOptions().position(locToLatLng(destination)).title("Destination"))
        map.moveCamera(CameraUpdateFactory.newLatLng(locToLatLng(destination)))
    }

    fun constructLocation(latitude: Double, longitude: Double) : Location {
        val newLocation = Location("")
        newLocation.setLatitude(latitude)
        newLocation.setLongitude(longitude)
        return newLocation
    }

    fun locToLatLng(location: Location) : LatLng {
        return LatLng(location.latitude, location.longitude)
    }
}

class DestinationLocationListener(activity: DestinationActivity) : LocationListener {
    private val TAG: String = "DestLocationListener"

    var activity: DestinationActivity = activity
    var lastLocation: Location? = null
    var lastStatus: Int? = null
    var providerEnabled: Boolean? = null

    override fun onLocationChanged(location: Location) {
        lastLocation = location

        activity.onLocationChange(location)

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
