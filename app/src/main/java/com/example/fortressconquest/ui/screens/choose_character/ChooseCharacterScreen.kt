package com.example.fortressconquest.ui.screens.choose_character

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.ui.components.LoadingDialog
import com.example.fortressconquest.ui.screens.home.components.ChooseCharacterClassDialog

@Composable
fun ChooseCharacterScreen(
    onSelectedCharacter: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChooseCharacterViewModel = hiltViewModel()
) {
    val dialogState by viewModel.characterDialogState.collectAsStateWithLifecycle(Response.Loading)

    when (val state = dialogState) {
        is Response.Success -> {
            when {
                state.data == null -> LoadingDialog()
                state.data.isEmpty() -> LaunchedEffect(state) {
                    onSelectedCharacter()
                }
                else -> ChooseCharacterClassDialog(
                    characterClasses = state.data,
                    onSelected = { character ->
                        viewModel.selectCharacterClass(character)
                        onSelectedCharacter()
                    }
                )
            }
        }
        else -> LoadingDialog()
    }
}