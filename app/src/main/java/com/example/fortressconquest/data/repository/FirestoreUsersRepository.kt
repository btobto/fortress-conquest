package com.example.fortressconquest.data.repository

import com.example.fortressconquest.common.Constants
import com.example.fortressconquest.di.CharacterClassesCollectionReference
import com.example.fortressconquest.di.UsersCollectionReference
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
    @UsersCollectionReference private val usersRef: CollectionReference,
    @CharacterClassesCollectionReference private val characterClassesRef: CollectionReference
): UsersRepository {
    override suspend fun getAllCharacterClasses(): List<CharacterClass> {
        return characterClassesRef.get().await().toObjects(CharacterClass::class.java)
    }

    override suspend fun getUser(id: String): User? {
        return usersRef.document(id).get().await().toObject()
    }

    override suspend fun createUser(user: User) {
        usersRef.document(user.id).set(user).await()
    }

    override suspend fun setUserCharacterClass(user: User, character: CharacterClass) {
        usersRef.document(user.id).update(Constants.USER_CHARACTER_FIELD, character).await()
    }

    override fun getUserFlow(id: String): Flow<User?> {
        return usersRef.document(id).dataObjects()
    }
}