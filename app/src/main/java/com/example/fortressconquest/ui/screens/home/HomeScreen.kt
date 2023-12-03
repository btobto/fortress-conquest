package com.example.fortressconquest.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.ui.components.LoadingDialog
import com.example.fortressconquest.ui.screens.home.components.ChooseCharacterClassDialog
import com.example.fortressconquest.ui.screens.home.components.HomeScreenContent

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val dialogState by homeViewModel.characterDialogState.collectAsStateWithLifecycle()

    HomeScreenContent(
        onClick = {
            homeViewModel.logout()
            onLogout()
        },
        modifier = modifier
    )

    when (val state = dialogState) {
        is Response.Success -> {
            state.data?.let {
                ChooseCharacterClassDialog(
                    characterClasses = state.data,
                    onSelected = homeViewModel::selectCharacterClass
                )
            }
        }
        is Response.Loading -> LoadingDialog()
        else -> Unit
    }
}
