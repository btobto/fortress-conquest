package com.example.fortressconquest.domain.repository

interface StorageRepository {
    suspend fun uploadUserImage(localUri: String, userId: String, filename: String? = null): String?
}