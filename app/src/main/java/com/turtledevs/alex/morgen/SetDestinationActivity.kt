package com.turtledevs.alex.morgen

import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class SetDestinationActivity : AppCompatActivity() {
    private val TAG: String = "SetDestinationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_destination)
    }

    fun setDestination(view: View) {
        var latitudeText = findViewById<EditText>(R.id.latitudeField).text.toString()
        var longitudeText = findViewById<EditText>(R.id.longitudeField).text.toString()
        var proximityText = findViewById<EditText>(R.id.proximityField).text.toString()

        var latitude: Double? = null
        var longitude: Double? = null
        var proximity: Float? = null
        try {
            latitude = latitudeText.toDouble()
            longitude = longitudeText.toDouble()
            proximity = proximityText.toFloat()
        }
        catch (e: NumberFormatException) {
            Log.e(TAG, e.message)
            return
        }

        intent = Intent(this, DestinationActivity::class.java).apply {
            putExtra("LATITUDE", latitude)
            putExtra("LONGITUDE", longitude)
            putExtra("PROXIMITY", proximity)
        }
        startActivity(intent)
    }
}
