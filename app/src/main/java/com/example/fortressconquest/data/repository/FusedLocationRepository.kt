package com.example.fortressconquest.data.repository

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.fortressconquest.R
import com.example.fortressconquest.di.IoDispatcher
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val TAG = "FusedLocationRepo"

class FusedLocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): LocationRepository {

    @RequiresPermission(allOf = [
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    override fun getCurrentLocation(): Flow<Response<Location, String>> = callbackFlow<Response<Location, String>> {
        fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                    CancellationTokenSource().token

                override fun isCancellationRequested(): Boolean = false
            }
        ).addOnSuccessListener { location ->
            if (location != null) {
                trySend(Response.Success(location))
            } else {
                Log.d(TAG, "Location is null")
                trySend(Response.Error(context.getString(R.string.error_loc_unavailable)))
            }
        }.addOnFailureListener { e ->
            Log.d(TAG, "Error getting location: ${e.message}")
            trySend(Response.Error(e.message ?: context.getString(R.string.error_loc_generic)))
        }.addOnCanceledListener {
            Log.d(TAG, "Canceled")
            trySend(Response.Error(context.getString(R.string.error_loc_cancelled)))
        }

        awaitClose {
            Log.d(TAG, "Stopped observing location")
        }
    }.flowOn(ioDispatcher)
}