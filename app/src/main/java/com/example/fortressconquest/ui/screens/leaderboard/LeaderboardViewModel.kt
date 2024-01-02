package com.example.fortressconquest.ui.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    usersRepository: UsersRepository
): ViewModel() {

    val usersFlow: Flow<PagingData<User>> = usersRepository
        .getUsersPaged()
        .cachedIn(viewModelScope)
}