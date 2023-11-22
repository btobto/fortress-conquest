package com.example.fortressconquest.data.repository

import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    private val userFlow: Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toUser())
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun register(email: String, password: String): Response<Boolean> = try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Error(e)
        }

    override suspend fun login(email: String, password: String): Response<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Error(e)
        }

    override fun logout() = auth.signOut()

    override fun getCurrentUser(scope: CoroutineScope): StateFlow<User?> =
        userFlow.toStateFlow(scope, auth.currentUser?.toUser())

    override fun isAuthenticated(scope: CoroutineScope): StateFlow<Boolean> =
        userFlow
            .map { user -> user != null }
            .toStateFlow(scope, auth.currentUser != null)

    private fun <T>Flow<T>.toStateFlow(scope: CoroutineScope, initialValue: T) =
        this.stateIn(scope, SharingStarted.WhileSubscribed(), initialValue)

    private fun FirebaseUser.toUser(): User =
        this.run {
            User(
                uid = uid,
                email = email!!,
                firstName = displayName!!,
                lastName = displayName!!,
                imageUrl = photoUrl!!
            )
        }
}