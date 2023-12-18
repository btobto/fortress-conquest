package com.example.fortressconquest.domain.repository

import android.location.Location
import com.example.fortressconquest.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentLocationUpdates(): Flow<Response<Location, String>>
    suspend fun getCurrentLocation(): Response<Location, String>
}