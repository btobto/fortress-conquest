package com.example.fortressconquest.di

import com.example.fortressconquest.data.repository.FirebaseStorageRepository
import com.example.fortressconquest.domain.repository.StorageRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideStorageRepository(): StorageRepository =
        FirebaseStorageRepository(Firebase.storage)
}