package com.example.fortressconquest.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.fortressconquest.ui.utils.BottomBarDestination

@Composable
fun BottomNavigationBar(
    destinations: List<BottomBarDestination>,
    isItemSelected: (String) -> Boolean,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        destinations.forEach { item ->
            val isSelected = isItemSelected(item.route)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onItemClick(item.route)
                },
                icon = {
                    Icon(
                        imageVector =
                            if (isSelected)
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