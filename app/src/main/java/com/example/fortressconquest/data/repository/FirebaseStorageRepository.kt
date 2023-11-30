package com.example.fortressconquest.data.repository

import android.net.Uri
import com.example.fortressconquest.domain.repository.StorageRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(
    private val storage: FirebaseStorage
) : StorageRepository {
    private val storageRef = storage.reference
    private val usersRef = storageRef.child("users")

    private suspend fun uploadImage(
        uri: Uri,
        folderReference: StorageReference,
        filename: String? = null
    ): Uri {
        val imageReference = folderReference.child(filename ?: "${uri.lastPathSegment}")
        imageReference.putFile(uri).await()

        return imageReference.downloadUrl.await()
    }

    override suspend fun uploadUserImage(localUri: Uri, userId: String, filename: String?): Uri {
        return uploadImage(localUri, usersRef.child("$userId"), filename)
    }
}