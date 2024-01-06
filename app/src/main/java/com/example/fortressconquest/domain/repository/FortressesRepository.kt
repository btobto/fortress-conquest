package com.example.fortressconquest.domain.repository

import android.location.Location
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.model.User

interface FortressesRepository {
    suspend fun setFortress(user: User, location: Location)

    suspend fun getFortress(id: String): Fortress?

    suspend fun getFortressesInRadius(location: Location, radiusInM: Double): List<Fortress>
}