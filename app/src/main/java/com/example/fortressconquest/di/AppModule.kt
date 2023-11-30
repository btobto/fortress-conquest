package com.example.fortressconquest.di

import com.example.fortressconquest.common.Constants
import com.example.fortressconquest.data.repository.FirebaseAuthRepository
import com.example.fortressconquest.data.repository.FirebaseStorageRepository
import com.example.fortressconquest.data.repository.FirebaseUsersRepository
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.StorageRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideUsersRef() = Firebase.firestore.collection(Constants.USERS_COLLECTION)

    @Provides
    fun provideUsersRepository(
        usersRef: CollectionReference
    ): UsersRepository = FirebaseUsersRepository(usersRef)

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository =
        FirebaseAuthRepository(Firebase.auth)

    @Provides
    @Singleton
    fun provideStorageRepository(): StorageRepository =
        FirebaseStorageRepository(Firebase.storage)
}