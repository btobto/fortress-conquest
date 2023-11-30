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
            userId = authResponse.uid,
        )

        val user = registrationData.run {
            User(
                uid = authResponse.uid,
                email = authResponse.email,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                photoUri = uploadedImageUri
            )
        }

        usersRepository.createUser(user)

        return user
    }
}