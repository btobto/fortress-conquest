package com.example.fortressconquest.domain.model

import android.net.Uri

data class RegistrationData(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val imageUri: Uri
)