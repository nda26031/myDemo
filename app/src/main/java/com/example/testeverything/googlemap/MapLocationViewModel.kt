package com.example.testeverything.googlemap

import android.content.Context
import android.location.Address
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MapLocationViewModel(private val locationRepository: MapLocationRepository) : ViewModel() {
    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> = _location

    private val _address = MutableLiveData<Address?>()
    val address: LiveData<Address?> = _address

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    init {
        fetchCurrentLocation()
    }

    fun fetchCurrentLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val location = locationRepository.getCurrentLocation()
                _location.value = location
                val address = location?.let { locationRepository.getAddress(it) }
                _address.value = address

                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error getting location: ${e.message}")
            }
        }
    }

    fun updateLocation(location: Location) {
        _location.value = location
    }
}

class MapViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = MapLocationRepository(context)
        return MapLocationViewModel(repo) as T
    }
}
