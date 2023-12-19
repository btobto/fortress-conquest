package com.example.fortressconquest.domain.model

import java.util.Date

data class Fortress(
    val id: String,
    val originalOwnerId: String,
    val currentOwnerId: String,
    val ownedSince: Date,
    val latitude: Double,
    val longitude: Double,
    val geoHash: String,
)