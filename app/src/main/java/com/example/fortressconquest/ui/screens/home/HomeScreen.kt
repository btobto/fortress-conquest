package com.example.fortressconquest.ui.screens.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel() ,
    modifier: Modifier = Modifier
) {
    Text(text = "home")
    Button(onClick = homeViewModel::logout) {
        Text(text = "log out")
    }
}