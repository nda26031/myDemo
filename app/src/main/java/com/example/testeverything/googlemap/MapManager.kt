package com.example.testeverything.googlemap

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapManager {
    var googleMap: GoogleMap? = null

    fun init(context: Context) {
        // setup nếu cần
    }

    fun onGetMapAsync(
        map: GoogleMap,
        mapType: Int,
        onMapLoaded: () -> Unit,
        snapshot: (Bitmap?) -> Unit
    ) {
        this.googleMap = map
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.setOnMapLoadedCallback {
            map.snapshot { snapshot(it) }
            onMapLoaded()
        }
    }

    fun  showCurrentLocationOnMap(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        googleMap?.clear()
        googleMap?.addMarker(MarkerOptions().position(latLng).title("You are here"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
    }

    fun showMarkerAt(latLng: LatLng) {
        googleMap?.clear()
        googleMap?.addMarker(MarkerOptions().position(latLng).title("You are here"))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
    }

    fun setOnClickMapListener(
        onMapClick: (LatLng) -> Unit,
    ) {
        googleMap?.setOnMapClickListener { latLng ->
            val clickedLocation = Location("").apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
            }
            onMapClick(latLng)
        }
    }
}