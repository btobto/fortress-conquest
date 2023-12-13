package com.example.fortressconquest.ui.screens.character_select

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.ui.components.LoadingScreen
import com.example.fortressconquest.ui.screens.character_select.components.CharacterSelectContent

@Composable
fun CharacterSelectScreen(
    onSelectedCharacter: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterSelectViewModel = hiltViewModel()
) {
    val dialogState by viewModel.characterDialogState.collectAsStateWithLifecycle(Response.Loading)

    AnimatedContent(
        targetState = dialogState,
        label = "Character select animated content"
    ) { targetState ->
        when (targetState) {
            is Response.Success -> {
                when {
                    targetState.data.isEmpty() -> LaunchedEffect(targetState) {
                        onSelectedCharacter()
                    }
                    else -> CharacterSelectContent(
                        characterClasses = targetState.data,
                        onSelectedCharacter = { character ->
                            viewModel.selectCharacterClass(character)
                            onSelectedCharacter()
                        },
                        modifier = modifier
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
            else -> LoadingScreen(
                modifier = modifier
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}