package com.example.fortressconquest.di

import com.example.fortressconquest.data.paging.UsersPagingSource
import com.example.fortressconquest.data.repository.FirestoreFortressesRepository
import com.example.fortressconquest.data.repository.FirestoreUsersRepository
import com.example.fortressconquest.domain.repository.FortressesRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideUsersRepository(
        @UsersCollectionReference usersRef: CollectionReference,
        @CharacterClassesCollectionReference characterClassesRef: CollectionReference,
        usersPagingSource: UsersPagingSource
    ): UsersRepository =
        FirestoreUsersRepository(
            usersRef,
            characterClassesRef,
            usersPagingSource
        )

    @Provides
    @ViewModelScoped
    fun provideFortressesRepository(
        @FortressesCollectionReference fortressesRef: CollectionReference,
        @UsersCollectionReference usersRef: CollectionReference
    ): FortressesRepository =
        FirestoreFortressesRepository(
            fortressesRef,
            usersRef,
            Firebase.firestore,
        )
}