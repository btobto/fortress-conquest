package com.example.fortressconquest.data.repository

import android.net.Uri
import android.util.Log
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
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

private const val TAG = "firebase"

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : AuthRepository {
    private companion object Paths {
        const val USERS = "users"
    }

    override suspend fun register(registrationData: RegistrationData): Response<Boolean> =
        try {
            registrationData.run {
                auth.createUserWithEmailAndPassword(email, password).await()
            }

            val user = auth.currentUser!!
            val imageUri = uploadImage(registrationData.imageUri, "${Paths.USERS}/${user.uid}")

            registrationData.run {
                user.updateProfile(userProfileChangeRequest {
                    displayName = "$firstName $lastName"
                    photoUri = imageUri
                }).await()
            }

            Response.Success(true)
        } catch (e: FirebaseAuthUserCollisionException) {
            Response.Error("Account already exists.")
        } catch (e: IOException) {
            Response.Error("No internet connection.")
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Registration error")
            Response.Error("An error occurred.")
        }

    override suspend fun login(loginData: LoginData): Response<Boolean> =
        try {
            loginData.run {
                auth.signInWithEmailAndPassword(email, password).await()
            }
            Response.Success(true)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Response.Error("Invalid email or password.")
        } catch (e: IOException) {
            Response.Error("No internet connection.")
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Login error")
            Response.Error("An error occurred.")
        }

    override suspend fun reloadUser(): Response<Boolean> =
        try {
            auth.currentUser?.reload()?.await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Error(e.message ?: "An error occurred.")
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

    private suspend fun uploadImage(
        uri: Uri,
        directory: String
    ): Uri {
        val ref = storage.reference.child("$directory/${uri.lastPathSegment}")
        ref.putFile(uri).await()

        return ref.downloadUrl.await()
    }

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