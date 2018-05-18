package com.turtledevs.alex.morgen

import android.location.Location
import com.google.android.gms.maps.model.LatLng

/*
 * Both of the location objects this app interacts with -- Location (from hardware) and LatLng (from
 * Google Maps) are clunky and have flaws.  To avoid being dependent on either, this class exists,
 * fully interoperable with both.
 */

class Position(val latitude: Double, val longitude: Double) {

    constructor(latlng: LatLng) : this(latlng.latitude, latlng.longitude)

    constructor(location: Location) : this(location.latitude, location.longitude)

    fun toLatLng() : LatLng {
        return LatLng(latitude, longitude)
    }
    fun toLocation() : Location {
        var l = Location("Position Object")
        l.latitude = latitude
        l.longitude = longitude
        return l
    }

    fun distanceTo(otherPosition: Position) : Float {
        val results = FloatArray(3)
        Location.distanceBetween(latitude, longitude, otherPosition.latitude, otherPosition.longitude, results)
        return results[0]
    }
}