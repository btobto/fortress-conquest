package com.example.fortressconquest.ui.screens.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.model.CharacterClass
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChooseCharacterClassDialog(
    characterClasses: List<CharacterClass>,
    onSelected: (CharacterClass) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val correctPageCount = characterClasses.size
    val startIndex = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        pageCount = { Int.MAX_VALUE },
        initialPage = startIndex
    )

    Dialog(
        onDismissRequest = { },
        DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = dimensionResource(id = R.dimen.dialog_character_width),
                contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.dialog_character_width)),
            ) { index ->
                val correctPage = calculatePage(index, startIndex, correctPageCount)
                val character = characterClasses[correctPage]

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = dimensionResource(id = R.dimen.dialog_character_min_height))
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Text(
                        text = character.name.replaceFirstChar(Char::titlecase),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Image(
                        painter = painterResource(id = getCharacterImageId(character.name)!!),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.padding_large)))

            PaginationSelectButtons(
                onSelected = {
                    val index = calculatePage(pagerState.currentPage, startIndex, correctPageCount)
                    onSelected(characterClasses[index])
                },
                onPrevious = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                onNext = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier.offset(y = -(16).dp)
            )
        }
    }
}

private fun calculatePage(
    index: Int,
    startIndex: Int,
    pageCount: Int
): Int {
    return (index - startIndex).let {
        when (pageCount) {
            0 -> it
            else -> it - it.floorDiv(pageCount) * pageCount
        }
    }
}

private fun getCharacterImageId(name: String): Int? {
    return when (name) {
        "knight" -> R.drawable.knight_500
        "assassin" -> R.drawable.assassin_500
        "barbarian" -> R.drawable.barbarian_500
        else -> null
    }
}