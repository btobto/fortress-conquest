package com.example.fortressconquest.ui.screens.character_select

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.utils.Response
import com.example.fortressconquest.ui.components.LoadingDialog
import com.example.fortressconquest.ui.components.LoadingScreen
import com.example.fortressconquest.ui.screens.character_select.components.CharacterSelectContent
import com.example.fortressconquest.ui.screens.map.components.ErrorWithIcon
import com.example.fortressconquest.ui.screens.register.RegisterViewModel

@Composable
fun CharacterSelectScreen(
    onRegisterSuccess: () -> Unit,
    onRegisterFailure: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    
    val characterClassesState by viewModel.characterClassesState.collectAsStateWithLifecycle()
    val responseState by viewModel.registerResponseState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = characterClassesState,
        label = "Character select animated content"
    ) { targetState ->
        when (targetState) {
            is Response.Success ->
                CharacterSelectContent(
                    characterClasses = targetState.data,
                    onSelectedCharacter = viewModel::submit,
                    modifier = modifier
                        .surfaceVariantBackground()
                )
            is Response.Loading -> LoadingScreen(
                modifier = modifier
                    .surfaceVariantBackground()
            )
            else -> ErrorWithIcon(
                text = stringResource(id = R.string.error_generic),
                modifier = modifier
                    .fillMaxSize()
                    .surfaceVariantBackground()
            )
        }
    }

    when (val response = responseState) {
        is Response.Loading -> LoadingDialog()
        is Response.Success -> LaunchedEffect(responseState) {
            onRegisterSuccess()
        }
        is Response.Error -> LaunchedEffect(responseState) {
            onRegisterFailure(response.error.asString(context))
            viewModel.resetError()
        }
        else -> Unit
    }
}

private fun Modifier.surfaceVariantBackground(): Modifier = composed { this.then(
    background(color = MaterialTheme.colorScheme.surfaceVariant)
)}