package com.example.fortressconquest.domain.model

import java.util.Date

data class Fortress(
    val id: String = "",
    val originalOwnerId: String = "",
    val currentOwnerId: String = "",
    val ownedSince: Date = Date(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val geoHash: String = "",
)