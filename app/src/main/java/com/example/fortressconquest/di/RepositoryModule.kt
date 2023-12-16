package com.example.fortressconquest.di

import android.content.Context
import com.example.fortressconquest.data.repository.FirebaseStorageRepository
import com.example.fortressconquest.data.repository.FusedLocationRepository
import com.example.fortressconquest.domain.repository.LocationRepository
import com.example.fortressconquest.domain.repository.StorageRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideStorageRepository(): StorageRepository =
        FirebaseStorageRepository(Firebase.storage)

    @Provides
    @ViewModelScoped
    fun provideLocationRepository(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context,
    ): LocationRepository =
        FusedLocationRepository(
            fusedLocationProviderClient,
            context
        )
}