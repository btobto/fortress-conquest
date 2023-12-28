package com.example.fortressconquest.domain.usecase

import com.example.fortressconquest.domain.model.User
import javax.inject.Inject

class SimulateBattleUseCase @Inject constructor() {
    operator fun invoke(user1: User, user2: User): User {
        return user1
    }
}