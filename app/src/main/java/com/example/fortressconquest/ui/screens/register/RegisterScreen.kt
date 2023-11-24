package com.example.fortressconquest.ui.screens.register

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    onNavigateToLoginScreen: () -> Unit,
    onUserRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(text = "register")
    Button(onClick = onNavigateToLoginScreen) {
        Text(text = "Login instead")
    }
}