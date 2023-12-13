package com.example.fortressconquest.data.repository

import android.net.Uri
import com.example.fortressconquest.common.Constants
import com.example.fortressconquest.domain.repository.StorageRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(
    storage: FirebaseStorage
) : StorageRepository {
    private val storageRef = storage.reference
    private val usersRef = storageRef.child(Constants.USERS_STORAGE)

    private suspend fun uploadImage(
        localUri: String,
        folderReference: StorageReference,
        filename: String? = null
    ): String {
        val imageUri = Uri.parse(localUri)
        val imageReference = folderReference.child(filename ?: "${imageUri.lastPathSegment}")
        imageReference.putFile(imageUri).await()

        return imageReference.downloadUrl.await().toString()
    }

    override suspend fun uploadUserImage(localUri: String, userId: String, filename: String?): String {
        return uploadImage(localUri, usersRef.child(userId), filename)
    }
}