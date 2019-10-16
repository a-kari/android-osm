package jp.neechan.osmtest.providers

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

@SuppressLint("MissingPermission")
class NetworkLocationProvider(context: Context) : IMyLocationProvider {
    
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationConsumer: IMyLocationConsumer? = null
    private var currentLocation:  Location? = null

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(currentLocation: Location?) {
            this@NetworkLocationProvider.currentLocation = currentLocation
            locationConsumer?.onLocationChanged(currentLocation, this@NetworkLocationProvider)
        }

        override fun onProviderEnabled(enabledProvider: String?) {}
        override fun onProviderDisabled(disabledProvider: String?) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
        this.locationConsumer = myLocationConsumer
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, // Get location updates from the network.
                1000L,                   // Min time to update location is 1 second.
                10.0f,                // Min changed distance to update location is 10 meters.
                locationListener                  // Listen to current location updates.
        )
        return true
    }

    override fun stopLocationProvider() {
        locationManager.removeUpdates(locationListener)
    }

    override fun getLastKnownLocation(): Location? {
        return currentLocation
    }

    override fun destroy() {}
}