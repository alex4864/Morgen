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

class DestinationActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG: String = "DestinationActivity"

    lateinit var destination: Position
    var userLocation: Position? = null
    var proximity: Float = 20.0f

    var locationListener: LocationListener = CallbackLocationListener(this::onLocationChange)
    var destinatonReached: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)

        destination = Position(intent.getDoubleExtra("LATITUDE", 0.0), intent.getDoubleExtra("LONGITUDE", 0.0))
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

    fun onLocationChange(loc: Location) {
        var location = Position(loc)

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
        map.addMarker(MarkerOptions().position(destination.toLatLng()).title("Destination"))
        map.moveCamera(CameraUpdateFactory.newLatLng(destination.toLatLng()))
    }

    /* fun constructLocation(latitude: Double, longitude: Double) : Location {
        val newLocation = Location("")
        newLocation.setLatitude(latitude)
        newLocation.setLongitude(longitude)
        return newLocation
    }

    fun locToLatLng(location: Location) : LatLng {
        return LatLng(location.latitude, location.longitude)
    } */
}
