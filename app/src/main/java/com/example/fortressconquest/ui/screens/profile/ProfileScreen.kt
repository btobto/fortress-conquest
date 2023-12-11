package com.example.fortressconquest.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier
    ) {

        Button(onClick = {
            profileViewModel.logout()
            onLogout()
        }) {
            Text(text = "log out")
        }
    }
}