package com.example.fortressconquest.data.repository

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.fortressconquest.R
import com.example.fortressconquest.di.IoDispatcher
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "FusedLocationRepo"

class FusedLocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context,
): LocationRepository {

    private companion object {
        const val LOCATION_REQUEST_INTERVAL_MS = 3000L
    }

    @RequiresPermission(allOf = [
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    override fun getCurrentLocation(): Flow<Response<Location, String>> = callbackFlow<Response<Location, String>> {
        val locationRequest = LocationRequest.Builder(LOCATION_REQUEST_INTERVAL_MS)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation

                if (location != null) {
                    trySend(Response.Success(location))
                } else {
                    trySend(Response.Error(context.getString(R.string.error_loc_off)))
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            trySend(Response.Error(e.message ?: context.getString(R.string.error_loc_generic)))
        }

        awaitClose {
            Log.d(TAG, "Stopped observing location")
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }.onEach { response ->
        val msg = when (response) {
            is Response.Success -> "Location: ${response.data.latitude}, ${response.data.longitude}"
            is Response.Error -> "Error: ${response.error}"
            is Response.Loading -> "Loading"
            else -> "Unknown"
        }
        Log.d(TAG, msg)
    }
}