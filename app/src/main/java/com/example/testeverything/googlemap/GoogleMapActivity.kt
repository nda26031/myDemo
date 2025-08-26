package com.example.testeverything.googlemap

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.testeverything.R
import com.example.testeverything.databinding.ActivityGoogleMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class GoogleMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGoogleMapBinding
    private lateinit var viewModel: MapLocationViewModel
    private lateinit var mapManager: MapManager

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("GPSMapActivity", "onCreate called")
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleMapBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory = MapViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[MapLocationViewModel::class.java]

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapManager = MapManager()
        mapManager.init(this)

        mapFragment.getMapAsync { googleMap ->
            mapManager.onGetMapAsync(googleMap, GoogleMap.MAP_TYPE_NORMAL, {}, {})
        }

        observeViewModel()

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mapManager.setOnClickMapListener {
            Log.d("GPSMapActivity", "Map clicked at: ${it.latitude}, ${it.longitude}")
            val clickedLocation = Location("").apply {
                latitude = it.latitude
                longitude = it.longitude
            }
            viewModel.updateLocation(clickedLocation)
            mapManager.showMarkerAt(it)
        }

    }

    private fun observeViewModel() {

        viewModel.isLoading.observe(this) {
            Log.d("GPSMapActivity", "Loading state changed: $it")
            if (it == true) {
                showTemplateLoading(true)
            } else {
                showTemplateLoading(false)
            }
        }

        Log.d("GPSMapActivity", "observeViewModel called")
        viewModel.location.observe(this) { location ->
            Log.d("GPSMapActivity", "Location updated: $location")
            location?.let { mapManager.showCurrentLocationOnMap(it) }
        }

        viewModel.address.observe(this) { address ->
            Log.d("GPSMapActivity", "Address updated: ${address?.adminArea}")
            address?.let {
                binding.clGpsInformation.visibility = View.VISIBLE
                binding.tvGpsName.text = it.getAddressLine(0)
                binding.tvLatitudeDetail.text = it.latitude.toString()
                binding.tvLongtitudeDetail.text = it.longitude.toString()
            }
        }
    }

    private fun showTemplateLoading(show: Boolean) {
        binding.skeletonTemplateLoading.apply {
            if (show) {
                shimmerDurationInMillis = 1000L
                showShimmer = true
                showSkeleton()
                visibility = View.VISIBLE
            } else {
                showOriginal()
                visibility = View.GONE
            }
        }
    }

}