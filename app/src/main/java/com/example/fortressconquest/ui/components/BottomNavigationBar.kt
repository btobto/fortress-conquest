package com.example.fortressconquest.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.fortressconquest.ui.utils.BottomBarDestination

@Composable
fun BottomNavigationBar(
    destinations: List<BottomBarDestination>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar(modifier = modifier) {
        destinations.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    onItemClick(item.route)
                    selectedItemIndex = index
                },
                icon = {
                    Icon(
                        imageVector =
                            if (index == selectedItemIndex)
                                item.selectedIcon
                            else
                                item.unselectedIcon,
                        contentDescription = item.route
                    )
                },
                label = {
                    Text(text = stringResource(id = item.labelId))
                }
            )
        }
    }
}