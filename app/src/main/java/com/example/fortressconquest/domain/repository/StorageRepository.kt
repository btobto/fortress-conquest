package com.example.fortressconquest.domain.repository

import android.net.Uri

interface StorageRepository {
    suspend fun uploadUserImage(localUri: Uri, userId: String, filename: String? = null): Uri
}