package com.example.fortressconquest.data.repository

import com.example.fortressconquest.domain.model.AuthResponse
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthRepository {
    override val authState: Flow<AuthState<AuthResponse>> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val state = auth.currentUser?.let { AuthState.LoggedIn(it.toAuthResponse()) } ?: AuthState.NotLoggedIn
            trySend(state)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun register(email: String, password: String): AuthResponse {
        auth.createUserWithEmailAndPassword(email, password).await()
        return auth.currentUser!!.toAuthResponse()
    }

    override suspend fun login(email: String, password: String): AuthResponse {
        auth.signInWithEmailAndPassword(email, password).await()
        return auth.currentUser!!.toAuthResponse()
    }

    override fun logout() = auth.signOut()

    private fun FirebaseUser.toAuthResponse(): AuthResponse =
        this.run {
            AuthResponse(
                id = uid
            )
        }
}