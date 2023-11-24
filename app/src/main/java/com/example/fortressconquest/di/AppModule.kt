package com.example.fortressconquest.di

import com.example.fortressconquest.data.repository.FirebaseAuthRepository
import com.example.fortressconquest.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = FirebaseAuthRepository(Firebase.auth)
}