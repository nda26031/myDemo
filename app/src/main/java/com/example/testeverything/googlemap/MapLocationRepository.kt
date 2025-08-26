package com.example.testeverything.googlemap

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale

class MapLocationRepository(private val context: Context) {
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { cont ->
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            0L
        ).apply {
            setMaxUpdates(1)
            setMinUpdateIntervalMillis(0)
        }.build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                fusedClient.removeLocationUpdates(this)
                cont.resume(result.lastLocation, null)
            }
        }

        fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

        cont.invokeOnCancellation {
            fusedClient.removeLocationUpdates(callback)
        }
    }

    fun getAddress(location: Location): Address? {
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
    }

    suspend fun getTemplateById(id: String): TemplateDataModel? {
        // Thay bằng retrofit/Room tùy bạn
        delay(300) // giả lập network/db
        return TemplateDataModel(
            id = id,
            name = "Trạm đo số 1",
            latitude = 21.028511,
            longitude = 105.804817,
            address = "Hà Nội, Việt Nam"
        )
    }
}