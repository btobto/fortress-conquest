package com.example.fortressconquest.data.repository

import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.UsersRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUsersRepository @Inject constructor(
    private val usersRef: CollectionReference
): UsersRepository {

    override suspend fun createUser(user: User) {
        usersRef.document(user.uid).set(user).await()
    }
}