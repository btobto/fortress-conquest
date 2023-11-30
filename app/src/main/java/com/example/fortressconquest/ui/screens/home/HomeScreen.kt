package com.example.fortressconquest.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fortressconquest.ui.screens.home.components.ChooseCharacterClassDialog

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChooseCharacterClassDialog(
        (0..4).toList(),
        {}
    )

    Column(
        modifier = modifier
    ) {

        Button(onClick = {
            homeViewModel.logout()
            onLogout()
        }) {
            Text(text = "log out")
        }
    }
}