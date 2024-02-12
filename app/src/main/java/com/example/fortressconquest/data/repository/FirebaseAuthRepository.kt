package com.example.fortressconquest.data.repository

import android.util.Log
import com.example.fortressconquest.di.UsersCollectionReference
import com.example.fortressconquest.domain.utils.AuthState
import com.example.fortressconquest.domain.model.RegistrationData
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.pow

private const val TAG = "AuthRepo"

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val storageRepository: StorageRepository,
    @UsersCollectionReference private val usersRef: CollectionReference,
    externalScope: CoroutineScope
) : AuthRepository {

    companion object {
        private const val STARTING_DELAY_MS = 1000
        private const val DELAY_FACTOR = 2.0
    }

    private val _registerInProgress = MutableStateFlow(false)
    private val _authFlow: Flow<String?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.uid)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthState<User>> =
        _authFlow.combine(_registerInProgress) { id, registerInProgress ->
            return@combine when {
                registerInProgress -> AuthState.Loading
                id != null -> AuthState.LoggedIn(id)
                else -> AuthState.NotLoggedIn
            }
        }.flatMapLatest { state ->
            when(state) {
                is AuthState.LoggedIn ->
                    usersRef.document(state.data).dataObjects<User>()
                        .map { user -> AuthState.LoggedIn(user!!) }
                        .retryWhen { _, attempt ->
                            if (attempt > 3) return@retryWhen false

                            val delayMs = STARTING_DELAY_MS * DELAY_FACTOR.pow(attempt.toInt()).toLong()
                            Log.i(TAG, "Retrying to get user, attempt: $attempt, delay: $delayMs")
                            delay(delayMs)

                            true
                        }
                        .onEach {
                            Log.i(TAG, "Get user success, user id: ${state.data}")
                        }
                        .catch { e ->
                            Log.i(TAG, "Retries failed")
                            logout()
                            throw e
                        }
                is AuthState.Loading -> flowOf(AuthState.Loading)
                is AuthState.NotLoggedIn -> flowOf(AuthState.NotLoggedIn)
            }
        }.stateIn(externalScope, SharingStarted.Lazily, AuthState.Loading)

    override suspend fun register(registrationData: RegistrationData) {
        try {
            _registerInProgress.emit(true)

            auth.createUserWithEmailAndPassword(
                registrationData.email,
                registrationData.password
            ).await()
            val id = auth.currentUser!!.uid


            val uploadedImageUri = registrationData.localPhotoUri?.let {
                storageRepository.uploadUserImage(
                    localUri = it,
                    userId = id,
                )
            }

            usersRef.document(id).set(
                User(
                    id = id,
                    firstName = registrationData.firstName,
                    lastName = registrationData.lastName,
                    photoUri = uploadedImageUri
                )
            ).await()
        } finally {
            _registerInProgress.emit(false)
        }
    }

    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override fun logout() = auth.signOut()
}