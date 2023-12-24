package com.example.fortressconquest.ui.screens.map.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.fortressconquest.R

@Composable
fun BottomMapBar(
    onProfileButtonClicked: () -> Unit,
    onLeaderboardButtonClicked: () -> Unit,
    onLogOutButtonClicked: () -> Unit,
    onMyLocationButtonClicked: () -> Unit,
    onLockCameraButtonClicked: () -> Unit,
    onFiltersButtonClicked: () -> Unit,
    onPlaceFortressButtonClicked: () -> Unit,
    cameraLocked: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    var expandedMenuState by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { expandedMenuState = true }) {
                        Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = stringResource(R.string.more_actions))
                    }
                    IconButton(onClick = onMyLocationButtonClicked) {
                        Icon(imageVector = Icons.Outlined.MyLocation, contentDescription = stringResource(R.string.current_location))
                    }
                    IconButton(onClick = onLockCameraButtonClicked) {
                        Icon(
                            imageVector =  if (cameraLocked) Icons.Outlined.LockOpen else Icons.Outlined.Lock,
                            contentDescription = stringResource(if (cameraLocked) R.string.unlock_camera else R.string.lock_camera)
                        )
                    }
                    IconButton(onClick = onFiltersButtonClicked) {
                        Icon(imageVector = Icons.Outlined.FilterList, contentDescription = stringResource(R.string.filters))
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onPlaceFortressButtonClicked,
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.place_fortress)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            content(innerPadding)
            DropdownMenu(
                expanded = expandedMenuState,
                onDismissRequest = { expandedMenuState = false },
                offset = DpOffset(10.dp, 0.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.profile)) }, 
                    onClick = onProfileButtonClicked
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.leaderboard)) },
                    onClick = onLeaderboardButtonClicked
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.log_out)) },
                    onClick = onLogOutButtonClicked
                )
            }
        }
    }
}