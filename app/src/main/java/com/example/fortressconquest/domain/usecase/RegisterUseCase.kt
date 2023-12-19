package com.example.fortressconquest.domain.usecase

import com.example.fortressconquest.domain.model.RegistrationData
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.StorageRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(registrationData: RegistrationData): User {
        val authResponse = authRepository.register(registrationData.email, registrationData.password)

        val uploadedImageUri = storageRepository.uploadUserImage(
            localUri = registrationData.localPhotoUri,
            userId = authResponse.id,
        )

        val user = registrationData.run {
            User(
                id = authResponse.id,
                firstName = firstName,
                lastName = lastName,
                photoUri = uploadedImageUri.toString()
            )
        }

        usersRepository.createUser(user)

        return user
    }
}