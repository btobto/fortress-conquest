package com.example.fortressconquest.di

import android.content.Context
import com.example.fortressconquest.common.Constants
import com.example.fortressconquest.data.paging.UsersPagingSource
import com.example.fortressconquest.data.repository.FirebaseAuthRepository
import com.example.fortressconquest.data.repository.FirebaseStorageRepository
import com.example.fortressconquest.data.repository.FusedLocationRepository
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.LocationRepository
import com.example.fortressconquest.domain.repository.StorageRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CharacterClassesCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FortressesCollectionReference

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideScope(
        @MainDispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcher)

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @UsersCollectionReference
    @Provides
    fun provideUsersRef(): CollectionReference =
        Firebase.firestore.collection(Constants.USERS_COLLECTION)

    @CharacterClassesCollectionReference
    @Provides
    fun provideCharacterClassesRef(): CollectionReference =
        Firebase.firestore.collection(Constants.CHARACTER_CLASSES_COLLECTION)

    @FortressesCollectionReference
    @Provides
    fun provideFortressesRef(): CollectionReference =
        Firebase.firestore.collection(Constants.FORTRESSES_COLLECTION)

    @Provides
    fun provideUsersPagingQuery(
        @UsersCollectionReference usersRef: CollectionReference
    ): Query =
        usersRef.orderBy(Constants.USER_XP_FIELD, Query.Direction.DESCENDING)

    @Provides
    fun provideUsersPagingSource(
        query: Query
    ): UsersPagingSource =
        UsersPagingSource(query)

    @Provides
    @Singleton
    fun provideAuthRepository(
        storageRepository: StorageRepository,
        @UsersCollectionReference usersRef: CollectionReference,
        scope: CoroutineScope
    ): AuthRepository =
        FirebaseAuthRepository(
            Firebase.auth,
            storageRepository,
            usersRef,
            scope
        )

    @Provides
    fun provideStorageRepository(): StorageRepository =
        FirebaseStorageRepository(Firebase.storage)

    @Provides
    @Singleton
    fun provideLocationRepository(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): LocationRepository =
        FusedLocationRepository(
            fusedLocationProviderClient,
            context,
            scope
        )
}