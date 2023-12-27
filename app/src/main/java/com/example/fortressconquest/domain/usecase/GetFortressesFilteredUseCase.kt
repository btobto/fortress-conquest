package com.example.fortressconquest.domain.usecase

import android.location.Location
import com.example.fortressconquest.domain.model.Filters
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.repository.FortressesRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import javax.inject.Inject

class GetFortressesFilteredUseCase @Inject constructor(
    private val fortressesRepository: FortressesRepository,
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(location: Location, filters: Filters): List<Fortress> {
        val fortresses = fortressesRepository.getFortressesInRadius(location, filters.radiusInM)

        val users = usersRepository.getUsers(fortresses.map { it.currentOwnerId })

        return fortresses.filter { fortress ->
            val user = users.find { it.id == fortress.currentOwnerId } ?: return@filter false
            user.level in filters.levelRange
        }
    }
}