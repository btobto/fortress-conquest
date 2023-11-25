package com.example.fortressconquest.data.repository

import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.RegistrationData
import com.example.fortressconquest.domain.model.LoginData
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(registrationData: RegistrationData): Response<Boolean> =
        try {
            registrationData.run {
                auth.createUserWithEmailAndPassword(email, password).await()
            }
            Response.Success(true)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Response.Error("Invalid email or password.")
        } catch (e: IOException) {
            Response.Error("No internet connection.")
        }

    override suspend fun login(loginData: LoginData): Response<Boolean> =
        try {
            loginData.run {
                auth.signInWithEmailAndPassword(email, password).await()
            }
            Response.Success(true)
        } catch (e: FirebaseAuthUserCollisionException) {
            Response.Error("Account already exists.")
        } catch (e: IOException) {
            Response.Error("No internet connection.")
        }

    override fun logout() = auth.signOut()

    override fun getAuthState(scope: CoroutineScope): StateFlow<AuthState> =
        callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser?.toUser()?.let { AuthState.LoggedIn(it) } ?: AuthState.NotLoggedIn)
            }
            auth.addAuthStateListener(authStateListener)
            awaitClose {
                auth.removeAuthStateListener(authStateListener)
            }
        }.stateIn(scope, SharingStarted.WhileSubscribed(), AuthState.Loading)

    private fun FirebaseUser.toUser(): User =
        this.run {
            User(
                uid = uid,
                email = email!!,
                name = displayName!!,
                photoUri = photoUrl!!
            )
        }
}