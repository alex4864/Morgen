package com.turtledevs.alex.morgen

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log

/*
 * A location listener that calls a callback, passed in at the constructor, whenever the location is
 * changed.  I would much rather do this with a function, but apparently Android devs have other
 * ideas about asynchronous programming.  Bastards.
 */

class CallbackLocationListener constructor(private val callback: (location: Location) -> Unit) : LocationListener {
    private val TAG: String = "CallbackLocationListener"

    private var lastLocation: Location? = null
    private var lastStatus: Int? = null
    private var providerEnabled: Boolean? = null

    override fun onLocationChanged(location: Location) {
        lastLocation = location

        callback(location)

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