package com.example.fortressconquest.data.repository

import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.UsersRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUsersRepository @Inject constructor(
    private val usersRef: CollectionReference,
    private val characterClassesRef: CollectionReference
): UsersRepository {
    override suspend fun getAllCharacterClasses(): List<CharacterClass> {
        return characterClassesRef.get().await().toObjects(CharacterClass::class.java)
    }

    override suspend fun getUser(uid: String): User? {
        return usersRef.document(uid).get().await().toObject()
    }

    override suspend fun createUser(user: User) {
        usersRef.document(user.uid).set(user).await()
    }

    override suspend fun setUserCharacterClass(user: User, character: CharacterClass) {
        usersRef.document(user.uid).update("character", character).await()
    }

    override fun getUserFlow(uid: String): Flow<User?> {
        return usersRef.document(uid).dataObjects()
    }
}