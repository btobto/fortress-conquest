package com.example.fortressconquest.domain.model

import android.net.Uri

data class User(
    val uid: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: Uri
)