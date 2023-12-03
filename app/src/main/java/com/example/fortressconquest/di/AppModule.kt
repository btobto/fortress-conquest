package com.example.fortressconquest.di

import android.util.Log
import com.example.fortressconquest.common.Constants
import com.example.fortressconquest.data.repository.FirebaseAuthRepository
import com.example.fortressconquest.data.repository.FirebaseStorageRepository
import com.example.fortressconquest.data.repository.FirestoreUsersRepository
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.StorageRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import com.example.fortressconquest.domain.usecase.GetCurrentUserUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CharacterClassesCollectionReference

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSingletonScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineExceptionHandler { _, e ->
            Log.e("singleton", "error: $e")}
        )

    @UsersCollectionReference
    @Provides
    fun provideUsersRef(): CollectionReference =
        Firebase.firestore.collection(Constants.USERS_COLLECTION)

    @CharacterClassesCollectionReference
    @Provides
    fun provideCharacterClassesRef(): CollectionReference =
        Firebase.firestore.collection(Constants.CHARACTER_CLASSES_COLLECTION)

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(
        scope: CoroutineScope,
        authRepository: AuthRepository,
        usersRepository: UsersRepository
    ): GetCurrentUserUseCase =
        GetCurrentUserUseCase(
            externalScope = scope,
            authRepository = authRepository,
            usersRepository = usersRepository
        )

    @Provides
    fun provideUsersRepository(
        @UsersCollectionReference usersRef: CollectionReference,
        @CharacterClassesCollectionReference classesRef: CollectionReference
    ): UsersRepository =
        FirestoreUsersRepository(usersRef, classesRef)

    @Provides
    @Singleton
    fun provideAuthRepository(
        scope: CoroutineScope
    ): AuthRepository =
        FirebaseAuthRepository(
            scope,
            Firebase.auth
        )

    @Provides
    @Singleton
    fun provideStorageRepository(): StorageRepository =
        FirebaseStorageRepository(Firebase.storage)
}