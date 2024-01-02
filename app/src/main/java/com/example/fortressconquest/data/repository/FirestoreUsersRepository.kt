package com.example.fortressconquest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.fortressconquest.common.Constants
import com.example.fortressconquest.data.paging.UsersPagingSource
import com.example.fortressconquest.di.CharacterClassesCollectionReference
import com.example.fortressconquest.di.UsersCollectionReference
import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.UsersRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUsersRepository @Inject constructor(
    @UsersCollectionReference private val usersRef: CollectionReference,
    @CharacterClassesCollectionReference private val characterClassesRef: CollectionReference,
    private val usersPagingSource: UsersPagingSource
): UsersRepository {
    override suspend fun getAllCharacterClasses(): List<CharacterClass> {
        return characterClassesRef.get().await().toObjects(CharacterClass::class.java)
    }

    override suspend fun getUser(id: String): User? {
        return usersRef.document(id).get().await().toObject()
    }

    override suspend fun getUsers(ids: List<String>): List<User> {
        val tasks = ids.map { id -> usersRef.document(id).get() }
        return Tasks.whenAllComplete(tasks).await()
            .mapNotNull { task -> (task.result as DocumentSnapshot).toObject(User::class.java) }
    }

    override fun getUsersPaged(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                initialLoadSize = Constants.INITIAL_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { usersPagingSource }
        ).flow
    }

    override suspend fun setUserCharacterClass(user: User, character: CharacterClass) {
        usersRef.document(user.id).update(Constants.USER_CHARACTER_FIELD, character).await()
    }
}