package com.example.fortressconquest.di

import com.example.fortressconquest.data.repository.FirebaseStorageRepository
import com.example.fortressconquest.data.repository.FirestoreFortressesRepository
import com.example.fortressconquest.domain.repository.FortressesRepository
import com.example.fortressconquest.domain.repository.StorageRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideStorageRepository(): StorageRepository =
        FirebaseStorageRepository(Firebase.storage)

    @ViewModelScoped
    @Provides
    fun provideFortressesRepository(
        @FortressesCollectionReference fortressesRef: CollectionReference
    ): FortressesRepository =
        FirestoreFortressesRepository(
            fortressesRef
        )
}